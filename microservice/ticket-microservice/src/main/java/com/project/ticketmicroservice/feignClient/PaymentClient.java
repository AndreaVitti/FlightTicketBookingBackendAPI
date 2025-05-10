package com.project.ticketmicroservice.feignClient;

import com.project.ticketmicroservice.DTO.PaymentRequest;
import com.project.ticketmicroservice.DTO.PaymentResponse;
import com.project.ticketmicroservice.config.CustomPaymentFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "payment-microservice",
        url = "${api.urls.paymentUrl}",
        configuration = CustomPaymentFeignConfig.class
)
public interface PaymentClient {

    @PostMapping("/makePayment")
    ResponseEntity<PaymentResponse> makePayment(@RequestHeader("Authorization") String jwToken, @RequestBody PaymentRequest paymentRequest);
}
