package com.project.ticketmicroservice.controller;

import com.project.ticketmicroservice.DTO.CheckoutCompletedRequest;
import com.project.ticketmicroservice.DTO.CheckoutRequest;
import com.project.ticketmicroservice.DTO.Response;
import com.project.ticketmicroservice.DTO.TicketCreateRequest;
import com.project.ticketmicroservice.service.TicketService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Response> createTicket(@RequestHeader("Authorization") String beaerToken, @RequestBody @Valid TicketCreateRequest ticketCreateRequest) {
        Response response = ticketService.createTicket(beaerToken, ticketCreateRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/{ConfCode}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getByConfCode(@PathVariable("ConfCode") String confCode) {
        Response response = ticketService.getByConfCode(confCode);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAll(@RequestParam(value = "PageNum", defaultValue = "0", required = false) int pageNum,
                                           @RequestParam(value = "PageSize", defaultValue = "15", required = false) int pageSize) {
        Response response = ticketService.getAll(pageNum, pageSize);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/ticketHistory/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> getAllByUserId(@PathVariable("userId") Long userId,
                                                   @RequestParam(value = "PageNum", defaultValue = "0", required = false) int pageNum,
                                                   @RequestParam(value = "PageSize", defaultValue = "15", required = false) int pageSize) {
        Response response = ticketService.getAllByUserId(userId, pageNum, pageSize);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PutMapping("/checkout")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> checkout(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid CheckoutRequest checkoutRequest) {
        Response response = ticketService.checkout(bearerToken, checkoutRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PutMapping("/checkoutCompleted")
    public ResponseEntity<Response> checkoutCompleted(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid CheckoutCompletedRequest completedRequest) {
        Response response = ticketService.checkoutCompleted(bearerToken, completedRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @DeleteMapping("/delete/{confirmCode}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<Response> deleteTicket(@RequestHeader("Authorization") String bearerToken, @PathVariable("confirmCode") String confirmCode) {
        Response response = ticketService.deleteTicket(bearerToken, confirmCode);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
