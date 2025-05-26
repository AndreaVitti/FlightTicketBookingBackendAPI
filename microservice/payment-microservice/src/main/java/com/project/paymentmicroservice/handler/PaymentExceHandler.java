package com.project.paymentmicroservice.handler;

import com.project.paymentmicroservice.DTO.Response;
import com.project.paymentmicroservice.exception.*;
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

    @ExceptionHandler(EventCreationException.class)
    public ResponseEntity<Response> eventCreationHandler(EventCreationException e) {
        Response response = new Response();
        response.setHttpCode(409);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(BadTicketRequest.class)
    public ResponseEntity<Response> badTicketRequestHandler(BadTicketRequest e) {
        Response response = new Response();
        response.setHttpCode(400);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(PaymenetNotFound.class)
    public ResponseEntity<Response> paymentNotFoundHandler(PaymenetNotFound e) {
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(LineItemsException.class)
    public ResponseEntity<Response> lineItemsHandler(LineItemsException e) {
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
