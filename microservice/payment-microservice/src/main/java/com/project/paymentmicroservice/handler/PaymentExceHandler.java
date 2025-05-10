package com.project.paymentmicroservice.handler;

import com.project.paymentmicroservice.DTO.Response;
import com.project.paymentmicroservice.exception.SessionError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PaymentExceHandler {

    @ExceptionHandler(SessionError.class)
    public ResponseEntity<Response> sessionErrHandler(SessionError e) {
        Response response = new Response();
        response.setHttpCode(500);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
