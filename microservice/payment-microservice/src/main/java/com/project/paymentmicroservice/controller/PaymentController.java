package com.project.paymentmicroservice.controller;

import com.project.paymentmicroservice.DTO.PaymentRequest;
import com.project.paymentmicroservice.DTO.Response;
import com.project.paymentmicroservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/makePayment")
    public ResponseEntity<Response> makePayment(@RequestHeader("Authorization") String bearerToken, @RequestBody @Valid PaymentRequest paymentRequest) {
        Response response = paymentService.makePayment(bearerToken, paymentRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Response> stripeEventHandler(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signatureHeader) {
        Response response = paymentService.stripeEventHandler(payload, signatureHeader);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAll(@RequestParam(value = "PageNum", defaultValue = "0", required = false) int pageNum,
                                           @RequestParam(value = "PageSize", defaultValue = "15", required = false) int pageSize) {
        Response response = paymentService.getAll(pageNum, pageSize);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @GetMapping("/getById/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getById(@PathVariable("id") Long id) {
        Response response = paymentService.getById(id);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
