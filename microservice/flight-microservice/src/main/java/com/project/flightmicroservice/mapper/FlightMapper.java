package com.project.flightmicroservice.mapper;

import com.project.flightmicroservice.DTO.AvailFlightResponse;
import com.project.flightmicroservice.DTO.BookingResponse;
import com.project.flightmicroservice.DTO.FlightDTO;
import com.project.flightmicroservice.entity.Flight;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class FlightMapper {

    public FlightDTO mapFlightToFlightDTO(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(flight.getId());
        flightDTO.setStartLoc(flight.getStartLoc());
        flightDTO.setDestination(flight.getDestination());
        flightDTO.setDepartureTime(flight.getDepartureTime());
        flightDTO.setArrivalTime(flight.getArrivalTime());
        flightDTO.setEconomySeats(flight.getEconomySeats());
        flightDTO.setBusinessSeats(flight.getBusinessSeats());
        flightDTO.setFirstClassSeats(flight.getFirstClassSeats());
        flightDTO.setPrice(flight.getPrice());
        return flightDTO;
    }

    public AvailFlightResponse mapFlightToAvailFlightResponse(Flight flight) {
        AvailFlightResponse availFlightResponse = new AvailFlightResponse();
        availFlightResponse.setId(flight.getId());
        availFlightResponse.setStartLoc(flight.getStartLoc());
        availFlightResponse.setDestination(flight.getDestination());
        availFlightResponse.setDepartureTime(flight.getDepartureTime());
        availFlightResponse.setArrivalTime(flight.getArrivalTime());
        return availFlightResponse;
    }

    public BookingResponse mapFlightToBookingResponse(Flight flight, BigDecimal ticketPrice){
        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setId(flight.getId());
        bookingResponse.setStartLoc(flight.getStartLoc());
        bookingResponse.setDestination(flight.getDestination());
        bookingResponse.setDepartureTime(flight.getDepartureTime());
        bookingResponse.setArrivalTime(flight.getArrivalTime());
        bookingResponse.setTicketPrice(ticketPrice);
        return bookingResponse;
    }

    public List<FlightDTO> mapFlightListToFlightDTOList(List<Flight> flights) {
        return flights.stream().map(flight -> mapFlightToFlightDTO(flight)).toList();
    }

    public List<AvailFlightResponse> mapFlightListToAvailFlightResponseList(List<Flight> flights, List<BigDecimal> totalPrices) {
        List<AvailFlightResponse> responseList = flights.stream().map(flight -> mapFlightToAvailFlightResponse(flight)).toList();
        for (int i = 0; i < responseList.size(); i++) {
            responseList.get(i).setPrice(totalPrices.get(i));
        }
        return responseList;
    }
}
