package com.project.paymentmicroservice.feignClient;

import com.project.paymentmicroservice.DTO.CheckoutComplReq;
import com.project.paymentmicroservice.DTO.Response;
import com.project.paymentmicroservice.config.CustomTicketFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "ticket-microservice",
        url = "${api.urls.ticketUrl}",
        configuration = CustomTicketFeignConfig.class
)
public interface TicketClient {

    @PutMapping("/checkoutCompleted")
    ResponseEntity<Response> checkoutCompletion(@RequestHeader("Authorization") String bearerToken, @RequestBody CheckoutComplReq checkoutComplReq);
}