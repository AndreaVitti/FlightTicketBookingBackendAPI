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
        if (createFlightRequest.getDepartureTime().isAfter(createFlightRequest.getArrivalTime())) {
            throw new ArrivalTimeInvalid("Arrival time is before departure time");
        }
        Response response = new Response();
        Flight flight = new Flight();
        flight.setStartLoc(createFlightRequest.getStartLoc());
        flight.setDestination(createFlightRequest.getDestination());
        flight.setDepartureTime(createFlightRequest.getDepartureTime());
        flight.setArrivalTime(createFlightRequest.getArrivalTime());
        flight.setEconomySeats(createFlightRequest.getEconomySeats());
        flight.setBusinessSeats(createFlightRequest.getBusinessSeats());
        flight.setFirstClassSeats(createFlightRequest.getFirstClassSeats());
        flight.setPrice(createFlightRequest.getPrice());
        flightRepository.save(flight);
        response.setHttpCode(200);
        response.setFlightDTO(flightMapper.mapFlightToFlightDTO(flight));
        return response;
    }

    public BookingResponse bookTicket(BookingRequest bookingRequest) {
        Flight flight = flightRepository.findIfSeatsEmpty(bookingRequest.getId(),
                        bookingRequest.getSeatClass().name(),
                        bookingRequest.getNumPeople())
                .orElseThrow(() -> new FlightNotFound("Flight not found"));
        BigDecimal price = getPrice(bookingRequest.getNumPeople(), bookingRequest.getSeatClass(), flight);
        BookingResponse bookingResponse = flightMapper.mapFlightToBookingResponse(flight, price);
        bookingResponse.setHttpCode(200);
        return bookingResponse;
    }

    public Response getAvailableFlights(String startLoc, String destination, LocalDate departureDate, int numPeople, SeatClass seatClass) {
        Response response = new Response();
        List<Flight> flightsSameLoc = flightRepository
                .findAvailability(startLoc, destination, seatClass.name(), numPeople)
                .orElseThrow(() -> new FlightNotFound("Flight not found"));
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


    public Response updateFlight(Long id, String startLoc, String destination, LocalDateTime departureTime, LocalDateTime arrivalTime, int economySeats, int businessSeats, int firstClassSeats, BigDecimal price) {
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
        if (economySeats > 1 && economySeats != flight.getEconomySeats()) {
            flight.setEconomySeats(economySeats);
        }
        if (businessSeats > 1 && businessSeats != flight.getBusinessSeats()) {
            flight.setBusinessSeats(businessSeats);
        }
        if (firstClassSeats > 1 && firstClassSeats != flight.getFirstClassSeats()) {
            flight.setFirstClassSeats(firstClassSeats);
        }
        if (price.compareTo(new BigDecimal("0.50")) >= 0 && !price.equals(flight.getPrice())) {
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
        Flight flight = flightRepository.findById(updateReq.getFlightId()).orElseThrow(() -> new FlightNotFound("Flight not found"));
        canAccessResource(loggedUserId, flight);
        switch (updateReq.getSeatClass()) {
            case ECONOMY:
                if (flight.getEconomySeats() + updateReq.getNumBookedSeats() >= 0) {
                    flight.setEconomySeats(flight.getEconomySeats() + updateReq.getNumBookedSeats());
                } else {
                    throw new NotEnoughSeats("No seats available");
                }
                break;
            case BUSINESS:
                if (flight.getBusinessSeats() + updateReq.getNumBookedSeats() >= 0) {
                    flight.setBusinessSeats(flight.getBusinessSeats() + updateReq.getNumBookedSeats());
                } else {
                    throw new NotEnoughSeats("No seats available");
                }
                break;
            case FIRST_CLASS:
                if (flight.getFirstClassSeats() + updateReq.getNumBookedSeats() >= 0) {
                    flight.setFirstClassSeats(flight.getFirstClassSeats() + updateReq.getNumBookedSeats());
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
