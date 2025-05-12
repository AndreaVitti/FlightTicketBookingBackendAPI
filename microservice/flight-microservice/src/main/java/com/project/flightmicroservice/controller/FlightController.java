package com.project.flightmicroservice.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.flightmicroservice.DTO.*;
import com.project.flightmicroservice.service.FlightService;
import com.project.flightmicroservice.type.SeatClass;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/flights")
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createFlight(@RequestBody @Valid CreateFlightRequest createFlightRequest) {
        Response response = flightService.createFlight(createFlightRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping("/book")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<BookingResponse> bookTicket(@RequestBody @Valid BookingRequest bookingRequest) {
        BookingResponse bookingResponse = flightService.bookTicket(bookingRequest);
        return ResponseEntity.status(bookingResponse.httpCode()).body(bookingResponse);
    }

    @GetMapping("/availableFlights")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getAvailableFlights(@RequestParam String startLoc,
                                                        @RequestParam String destination,
                                                        @RequestParam LocalDate departureDate,
                                                        @RequestParam int numPeople,
                                                        @RequestParam SeatClass seatClass) {
        Response response = flightService.getAvailableFlights(startLoc, destination, departureDate, numPeople, seatClass);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteFlight(@PathVariable("id") Long id) {
        Response response = flightService.deleteFlight(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateFlight(@PathVariable("id") Long id,
                                                 @RequestParam(required = false) String startLoc,
                                                 @RequestParam(required = false) String destination,
                                                 @RequestParam(required = false) LocalDateTime departureTime,
                                                 @RequestParam(required = false) LocalDateTime arrivalTime,
                                                 @RequestParam(required = false) Integer economySeats,
                                                 @RequestParam(required = false) Integer businessSeats,
                                                 @RequestParam(required = false) Integer firstClassSeats,
                                                 @RequestParam(required = false) BigDecimal price) {
        Response response = flightService.updateFlight(id, startLoc, destination, departureTime, arrivalTime, economySeats, businessSeats, firstClassSeats, price);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PutMapping("/updateBookedSeat")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> updateBookedSeat(@RequestBody @Valid UpdateBookedSeatsReq updateReq) {
        Response response = flightService.updateBookedSeat(updateReq);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
