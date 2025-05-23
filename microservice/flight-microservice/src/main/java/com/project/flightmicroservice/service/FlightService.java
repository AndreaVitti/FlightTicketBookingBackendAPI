package com.project.flightmicroservice.service;

import com.project.flightmicroservice.DTO.*;
import com.project.flightmicroservice.entity.Flight;
import com.project.flightmicroservice.exceptions.ArrivalTimeInvalid;
import com.project.flightmicroservice.exceptions.CantAccessResource;
import com.project.flightmicroservice.exceptions.FlightNotFound;
import com.project.flightmicroservice.exceptions.NotEnoughSeats;
import com.project.flightmicroservice.mapper.FlightMapper;
import com.project.flightmicroservice.repository.FlightRepository;
import com.project.flightmicroservice.type.Role;
import com.project.flightmicroservice.type.SeatClass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final FlightMapper flightMapper;
    @Value("${api.price.business}")
    private String businessPrice;
    @Value("${api.price.firstClass}")
    private String firstClassPrice;


    public Response createFlight(CreateFlightRequest createFlightRequest) {
        if (createFlightRequest.departureTime().isAfter(createFlightRequest.arrivalTime())) {
            throw new ArrivalTimeInvalid("Arrival time is before departure time");
        }
        Response response = new Response();
        Flight flight = new Flight();
        flight.setStartLoc(createFlightRequest.startLoc());
        flight.setDestination(createFlightRequest.destination());
        flight.setDepartureTime(createFlightRequest.departureTime());
        flight.setArrivalTime(createFlightRequest.arrivalTime());
        flight.setEconomySeats(createFlightRequest.economySeats());
        flight.setBusinessSeats(createFlightRequest.businessSeats());
        flight.setFirstClassSeats(createFlightRequest.firstClassSeats());
        flight.setPrice(createFlightRequest.price());
        flightRepository.save(flight);
        response.setHttpCode(200);
        response.setFlightDTO(flightMapper.mapFlightToFlightDTO(flight));
        return response;
    }

    public BookingResponse bookTicket(BookingRequest bookingRequest) {
        Flight flight = flightRepository.findIfSeatsEmpty(
                        bookingRequest.id(),
                        bookingRequest.seatClass().name(),
                        bookingRequest.numPeople())
                .orElseThrow(() -> new FlightNotFound("Flight not found"));
        BigDecimal price = getPrice(bookingRequest.numPeople(), bookingRequest.seatClass(), flight);
        return flightMapper.mapFlightToBookingResponse(flight, price);
    }

    public Response getAvailableFlights(String startLoc, String destination, LocalDate departureDate, int numPeople, SeatClass seatClass, int pageNum, int pageSize) {
        Response response = new Response();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Flight> flightPage = flightRepository
                .findAvailability(startLoc, destination, seatClass.name(), numPeople, pageable)
                .orElseThrow(() -> new FlightNotFound("Flight not found"));
        List<Flight> flightsSameLoc = flightPage.getContent();
        List<Flight> flightAvail = flightsSameLoc
                .stream()
                .filter(flight -> flight.getDepartureTime().toLocalDate().isEqual(departureDate))
                .toList();
        if (flightAvail.isEmpty()) {
            throw new FlightNotFound("Flight not found");
        }
        List<BigDecimal> totalPrices = getPrices(numPeople, seatClass, flightAvail);
        response.setHttpCode(200);
        response.setAvailFlightResponseList(flightMapper.mapFlightListToAvailFlightResponseList(flightAvail, totalPrices));
        return response;
    }

    private BigDecimal getPrice(int numPeople, SeatClass seatClass, Flight flight) {
        BigDecimal pricePerPerson = flight.getPrice().multiply(new BigDecimal(numPeople));
        switch (seatClass) {
            case BUSINESS:
                pricePerPerson = pricePerPerson.multiply(new BigDecimal(businessPrice));
                break;
            case FIRST_CLASS:
                pricePerPerson = pricePerPerson.multiply(new BigDecimal(firstClassPrice));
                break;
        }
        return pricePerPerson;
    }

    private List<BigDecimal> getPrices(int numPeople, SeatClass seatClass, List<Flight> flights) {
        return flights
                .stream()
                .map(flight -> getPrice(numPeople, seatClass, flight)).toList();
    }

    public Response deleteFlight(Long id) {
        Response response = new Response();
        flightRepository.findById(id).orElseThrow(() -> new FlightNotFound("Flight not found"));
        flightRepository.deleteById(id);
        response.setHttpCode(200);
        return response;
    }


    public Response updateFlight(Long id, String startLoc,
                                 String destination,
                                 LocalDateTime departureTime,
                                 LocalDateTime arrivalTime,
                                 Integer economySeats,
                                 Integer businessSeats,
                                 Integer firstClassSeats,
                                 BigDecimal price) {
        Response response = new Response();
        Flight flight = flightRepository.findById(id).orElseThrow(() -> new FlightNotFound("Flight not found"));
        if (startLoc != null && !startLoc.equals(flight.getStartLoc())) {
            flight.setStartLoc(startLoc);
        }
        if (destination != null && !destination.equals(flight.getDestination())) {
            flight.setDestination(destination);
        }
        if (departureTime != null && !departureTime.equals(flight.getDepartureTime()) && departureTime.isAfter(LocalDateTime.now())) {
            flight.setDepartureTime(departureTime);
        }
        if (arrivalTime != null && !arrivalTime.equals(flight.getArrivalTime()) && arrivalTime.isAfter(departureTime)) {
            flight.setArrivalTime(arrivalTime);
        }
        if (economySeats != null && economySeats > 1 && !economySeats.equals(flight.getEconomySeats())) {
            flight.setEconomySeats(economySeats);
        }
        if (businessSeats != null && businessSeats > 1 && !businessSeats.equals(flight.getBusinessSeats())) {
            flight.setBusinessSeats(businessSeats);
        }
        if (firstClassSeats != null && firstClassSeats > 1 && !firstClassSeats.equals(flight.getFirstClassSeats())) {
            flight.setFirstClassSeats(firstClassSeats);
        }
        if (price != null && price.compareTo(new BigDecimal("0.50")) >= 0 && !price.equals(flight.getPrice())) {
            flight.setPrice(price);
        }
        flightRepository.save(flight);
        response.setHttpCode(200);
        response.setFlightDTO(flightMapper.mapFlightToFlightDTO(flight));
        return response;
    }

    public Response updateBookedSeat(UpdateBookedSeatsReq updateReq) {
        Response response = new Response();
        Long loggedUserId = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Flight flight = flightRepository.findById(updateReq.flightId()).orElseThrow(() -> new FlightNotFound("Flight not found"));
        canAccessResource(loggedUserId, flight);
        switch (updateReq.seatClass()) {
            case ECONOMY:
                if (flight.getEconomySeats() + updateReq.numBookedSeats() >= 0) {
                    flight.setEconomySeats(flight.getEconomySeats() + updateReq.numBookedSeats());
                } else {
                    throw new NotEnoughSeats("No seats available");
                }
                break;
            case BUSINESS:
                if (flight.getBusinessSeats() + updateReq.numBookedSeats() >= 0) {
                    flight.setBusinessSeats(flight.getBusinessSeats() + updateReq.numBookedSeats());
                } else {
                    throw new NotEnoughSeats("No seats available");
                }
                break;
            case FIRST_CLASS:
                if (flight.getFirstClassSeats() + updateReq.numBookedSeats() >= 0) {
                    flight.setFirstClassSeats(flight.getFirstClassSeats() + updateReq.numBookedSeats());
                } else {
                    throw new NotEnoughSeats("No seats available");
                }
                break;
        }
        flightRepository.save(flight);
        response.setHttpCode(200);
        return response;
    }

    private void canAccessResource(Long loggedUserId, Flight flight) {
        if (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .noneMatch(authority -> authority
                        .equals(new SimpleGrantedAuthority(Role.ADMIN.name()))) &&
                !loggedUserId.equals(flight.getId())) {
            throw new CantAccessResource("Access to resource denied");
        }
    }
}
