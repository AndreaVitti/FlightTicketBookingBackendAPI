package com.project.ticketmicroservice.handler;

import com.project.ticketmicroservice.DTO.Response;
import com.project.ticketmicroservice.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TicketExceHandler {

    @ExceptionHandler(FlightNotAvail.class)
    public ResponseEntity<Response> flightNotAvailHandler(FlightNotAvail e) {
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<Response> userNotFoundHandler(UserNotFound e) {
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(TicketNotFound.class)
    public ResponseEntity<Response> ticketNotFoundHandler(UserNotFound e) {
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(CantAccessResource.class)
    public ResponseEntity<Response> cantAccessResourceHandler(UserNotFound e) {
        Response response = new Response();
        response.setHttpCode(403);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(ServerErr.class)
    public ResponseEntity<Response> serverErrHandler(ServerErr e) {
        Response response = new Response();
        response.setHttpCode(Integer.parseInt(e.getMessage().substring(13)));
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(SessionErr.class)
    public ResponseEntity<Response> sessionErrHandler(SessionErr e) {
        Response response = new Response();
        response.setHttpCode(Integer.parseInt(e.getMessage().substring(14)));
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
