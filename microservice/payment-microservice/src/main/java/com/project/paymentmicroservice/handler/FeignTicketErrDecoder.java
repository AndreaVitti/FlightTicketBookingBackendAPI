package com.project.paymentmicroservice.handler;

import com.project.paymentmicroservice.exception.BadTicketRequest;
import com.project.paymentmicroservice.exception.ServerErr;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignTicketErrDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        if (status.is5xxServerError()) {
            return new ServerErr("Session error " + status);
        } else if (status.is4xxClientError()) {
            return new BadTicketRequest("Ticket not found or invalid");
        } else {
            return new RuntimeException("Generic exception");
        }
    }
}
