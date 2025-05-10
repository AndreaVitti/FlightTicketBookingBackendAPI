package com.project.ticketmicroservice.handler;

import com.project.ticketmicroservice.exception.ServerErr;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignPaymentErrDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        if (status.is5xxServerError()) {
            return new ServerErr("Session error " + status);
        } else {
            return new Exception("Generic exception");
        }
    }
}
