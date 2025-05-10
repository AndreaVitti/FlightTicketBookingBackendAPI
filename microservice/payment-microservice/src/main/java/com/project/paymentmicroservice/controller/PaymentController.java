package com.project.paymentmicroservice.controller;

import com.project.paymentmicroservice.DTO.PaymentRequest;
import com.project.paymentmicroservice.DTO.Response;
import com.project.paymentmicroservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/makePayment")
    public ResponseEntity<Response> makePayment(@RequestBody PaymentRequest paymentRequest){
        Response response = paymentService.makePayment(paymentRequest);
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
