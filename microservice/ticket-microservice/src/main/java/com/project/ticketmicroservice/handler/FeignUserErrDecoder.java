package com.project.ticketmicroservice.handler;

import com.project.ticketmicroservice.exception.ServerErr;
import com.project.ticketmicroservice.exception.UserNotFound;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class FeignUserErrDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        if (status.is5xxServerError()) {
            return new ServerErr("Server error " + status);
        } else if (status.is4xxClientError()) {
            return new UserNotFound("User not found");
        } else {
            return new Exception("Generic exception");
        }
    }
}
