package com.project.ticketmicroservice.service;

import com.project.ticketmicroservice.DTO.*;
import com.project.ticketmicroservice.entity.InfoPassenger;
import com.project.ticketmicroservice.entity.Ticket;
import com.project.ticketmicroservice.exception.CantAccessResource;
import com.project.ticketmicroservice.exception.TicketNotFound;
import com.project.ticketmicroservice.feignClient.PaymentClient;
import com.project.ticketmicroservice.feignClient.UserClient;
import com.project.ticketmicroservice.handler.RestTemplateResponseErrorHandler;
import com.project.ticketmicroservice.mapper.TicketMapper;
import com.project.ticketmicroservice.repository.InfoPassengerRepository;
import com.project.ticketmicroservice.repository.TicketRepository;
import com.project.ticketmicroservice.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketMapper ticketMapper;
    private final UserClient userClient;
    private final PaymentClient paymentClient;
    private final TicketRepository ticketRepository;
    private final InfoPassengerRepository infoPassengerRepository;
    @Value("${api.urls.flightUrl}")
    private String flightUrl;

    @Transactional
    public Response createTicket(String bearerToken, TicketCreateRequest ticketCreateRequest) {
        Ticket ticket = new Ticket();
        Response response = new Response();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);
        HttpEntity<FlightBookRequest> requestEntity = new HttpEntity<>(ticketMapper.mapTicketCreateToFlightBook(ticketCreateRequest), headers);
        ParameterizedTypeReference<FlightBookResponse> responseType = new ParameterizedTypeReference<>() {
        };
        ResponseEntity<FlightBookResponse> flightResp = restTemplate.exchange(flightUrl + "/book", POST, requestEntity, responseType);

        UserDTO userDTO = userClient.getUserById(bearerToken, ticketCreateRequest.userId()).getBody().userDTO();

        ticket.setConfirmCode(UUID.randomUUID().toString());
        ticket.setUserId(userDTO.id());
        ticket.setFlightId(ticketCreateRequest.flightId());
        ticket.setPrice(flightResp.getBody().ticketPrice());
        ticket.setSeatClass(ticketCreateRequest.seatClass());
        ticketRepository.save(ticket);

        ticketCreateRequest.infoPassengerDTOList().forEach(infoPassDTO -> {
            InfoPassenger info = new InfoPassenger();
            info.setFirstName(infoPassDTO.firstName());
            info.setLastName(infoPassDTO.lastName());
            info.setBirthday(infoPassDTO.birthday());
            info.setPassportId(infoPassDTO.passportId());
            info.setPassExpireDate(infoPassDTO.passExpireDate());
            info.setTicket(ticket);
            infoPassengerRepository.save(info);
        });

        response.setHttpCode(200);
        response.setTicketId(ticket.getId());
        response.setConfirmationCode(ticket.getConfirmCode());
        return response;
    }

    public Response getByConfCode(String confCode) {
        Response response = new Response();
        Ticket ticket = isTicketValid(confCode);
        response.setHttpCode(200);
        response.setTicketFullInfo(ticketMapper.mapTicketToTicketFullInfo(ticket));
        return response;
    }

    public Response getAll(int pageNum, int pageSize) {
        Response response = new Response();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Ticket> ticketPage = ticketRepository.findAll(pageable);
        List<Ticket> tickets = ticketPage.getContent();
        if (tickets.isEmpty()) {
            throw new TicketNotFound("No ticket found");
        }
        response.setHttpCode(200);
        response.setTicketFullInfoList(ticketMapper.mapTicketListToTicketFullInfoList(tickets));
        return response;
    }

    public Response getAllByUserId(Long userId, int pageNum, int pageSize) {
        Response response = new Response();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Long loggedUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<Ticket> ticketPage = ticketRepository.findByUserId(userId, pageable).orElseThrow(() -> new TicketNotFound("No ticket found"));
        List<Ticket> tickets = ticketPage.getContent();
        canAccessMultipleResources(loggedUserId, tickets);
        response.setHttpCode(200);
        response.setTicketFullInfoList(ticketMapper.mapTicketListToTicketFullInfoList(tickets));
        return response;
    }

    public Response checkout(String bearerToken, CheckoutRequest checkoutRequest) {
        Response response = new Response();
        Ticket ticket = isTicketValid(checkoutRequest.ticketConfirmCode());
        PaymentRequest paymentRequest = ticketMapper.mapCheckoutReqToPaymentReq(checkoutRequest, ticket.getPrice());
        PaymentResponse payResp = paymentClient.makePayment(bearerToken, paymentRequest).getBody();
        response.setHttpCode(200);
        response.setPaymentId(payResp.getPaymentId());
        response.setSessionId(payResp.getSessionId());
        response.setSessionUrl(payResp.getSessionUrl());
        return response;
    }

    public Response checkoutCompleted(String bearerToken, CheckoutCompletedRequest completedRequest) {
        Response response = new Response();
        Ticket ticket = isTicketValid(completedRequest.ticketConfirmationCode());
        ticket.setPaymentId(completedRequest.paymentId());
        ticketRepository.save(ticket);
        callUpdateSeatsRestTemplate(bearerToken, ticket, false);
        response.setHttpCode(200);
        return response;
    }

    @Transactional
    public Response deleteTicket(String bearerToken, String confirmCode) {
        Response response = new Response();
        Ticket ticket = isTicketValid(confirmCode);
        ticketRepository.deleteByConfirmCode(confirmCode);
        if (ticket.getPaymentId() != null) {
            callUpdateSeatsRestTemplate(bearerToken, ticket, true);
        }
        response.setHttpCode(200);
        return response;
    }

    private Ticket isTicketValid(String confCode) {
        Long loggedUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Ticket ticket = ticketRepository.findByConfirmCode(confCode).orElseThrow(() -> new TicketNotFound("Ticket not found"));
        canAccessResource(loggedUserId, ticket);
        return ticket;
    }

    private void canAccessMultipleResources(Long loggedUserId, List<Ticket> tickets) {
        tickets.forEach(ticket -> canAccessResource(loggedUserId, ticket));
    }

    private void canAccessResource(Long loggedUserId, Ticket ticket) {
        if (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .noneMatch(authority -> authority
                        .equals(new SimpleGrantedAuthority(Role.ADMIN.name()))) &&
                !loggedUserId.equals(ticket.getUserId())) {
            throw new CantAccessResource("Access to resource denied");
        }
    }

    private void callUpdateSeatsRestTemplate(String bearerToken, Ticket ticket, boolean addOrSub) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearerToken);

        UpdateSeatsReq seatsReq = new UpdateSeatsReq();

        seatsReq.setFlightId(ticket.getFlightId());
        if (addOrSub) {
            seatsReq.setNumBookedSeats(ticket.getInfoPassengerList().size());
        } else {
            seatsReq.setNumBookedSeats(-ticket.getInfoPassengerList().size());
        }
        seatsReq.setSeatClass(ticket.getSeatClass());

        HttpEntity<UpdateSeatsReq> requestEntity = new HttpEntity<>(seatsReq, headers);
        ParameterizedTypeReference<Response> responseType = new ParameterizedTypeReference<>() {
        };

        restTemplate.exchange(flightUrl + "/updateBookedSeat", PUT, requestEntity, responseType);
    }
}
