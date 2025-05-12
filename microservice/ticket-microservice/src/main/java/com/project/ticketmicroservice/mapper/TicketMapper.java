package com.project.ticketmicroservice.mapper;

import com.project.ticketmicroservice.DTO.*;
import com.project.ticketmicroservice.entity.InfoPassenger;
import com.project.ticketmicroservice.entity.Ticket;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TicketMapper {
    public FlightBookRequest mapTicketCreateToFlightBook(TicketCreateRequest createReq) {
        return new FlightBookRequest(
                createReq.flightId(),
                createReq.infoPassengerDTOList().size(),
                createReq.seatClass());
    }

    public PaymentRequest mapCheckoutReqToPaymentReq(CheckoutRequest checkoutReq, BigDecimal price) {
        PaymentRequest payReq = new PaymentRequest(checkoutReq.ticketConfirmCode(), checkoutReq.currency(), price);
        return payReq;
    }

    public TicketFullInfo mapTicketToTicketFullInfo(Ticket ticket) {
        return new TicketFullInfo(
                ticket.getId(),
                ticket.getConfirmCode(),
                ticket.getUserId(),
                ticket.getFlightId(),
                ticket.getPaymentId(),
                ticket.getPrice(),
                ticket.getSeatClass(),
                mapInfoPassListToInfoPassDTOList(ticket.getInfoPassengerList()));
    }

    public InfoPassengerDTO mapInfoPassToInfoPassDTO(InfoPassenger info) {
        return new InfoPassengerDTO(
                info.getFirstName(),
                info.getLastName(),
                info.getBirthday(),
                info.getPassportId(),
                info.getPassExpireDate());
    }

    public List<InfoPassengerDTO> mapInfoPassListToInfoPassDTOList(List<InfoPassenger> infos) {
        return infos.stream().map(info -> mapInfoPassToInfoPassDTO(info)).toList();
    }

    public List<TicketFullInfo> mapTicketListToTicketFullInfoList(List<Ticket> tickets) {
        return tickets.stream().map(ticket -> mapTicketToTicketFullInfo(ticket)).toList();
    }
}
