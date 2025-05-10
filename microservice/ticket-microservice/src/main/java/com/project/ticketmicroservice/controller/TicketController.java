package com.project.ticketmicroservice.controller;

import com.project.ticketmicroservice.DTO.PaymentRequest;
import com.project.ticketmicroservice.DTO.Response;
import com.project.ticketmicroservice.DTO.TicketCreateRequest;
import com.project.ticketmicroservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> createTicket(@RequestHeader("Authorization") String beaerToken, @RequestBody TicketCreateRequest ticketCreateRequest) {
        Response response = ticketService.createTicket(beaerToken, ticketCreateRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getByConfirmCode/{ConfCode}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getByConfCode(@PathVariable("ConfCode") String confCode) {
        Response response = ticketService.getByConfCode(confCode);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAll() {
        Response response = ticketService.getAll();
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/allByUserId/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getAllByUserId(@PathVariable("userId")Long userId) {
        Response response = ticketService.getAllByUserId(userId);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PutMapping("/checkout")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> checkout(@RequestHeader("Authorization") String bearerToken, @RequestBody PaymentRequest paymentRequest) {
        Response response = ticketService.checkout(bearerToken, paymentRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
