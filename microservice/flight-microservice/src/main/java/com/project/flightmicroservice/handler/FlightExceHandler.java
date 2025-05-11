package com.project.flightmicroservice.handler;

import com.project.flightmicroservice.DTO.Response;
import com.project.flightmicroservice.exceptions.ArrivalTimeInvalid;
import com.project.flightmicroservice.exceptions.CantAccessResource;
import com.project.flightmicroservice.exceptions.FlightNotFound;
import com.project.flightmicroservice.exceptions.NotEnoughSeats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FlightExceHandler {

    @ExceptionHandler(FlightNotFound.class)
    public ResponseEntity<Response> handlerFlightNotFound(FlightNotFound e) {
        Response response = new Response();
        response.setHttpCode(404);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(ArrivalTimeInvalid.class)
    public ResponseEntity<Response> handlerArrivalTimeInvalid(FlightNotFound e) {
        Response response = new Response();
        response.setHttpCode(400);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(CantAccessResource.class)
    public ResponseEntity<Response> cantAccessResourceHandler(CantAccessResource e) {
        Response response = new Response();
        response.setHttpCode(403);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }

    @ExceptionHandler(NotEnoughSeats.class)
    public ResponseEntity<Response> notEnoughSeatsHandler(NotEnoughSeats e) {
        Response response = new Response();
        response.setHttpCode(400);
        response.setMessage(e.getMessage());
        return ResponseEntity.status(response.getHttpCode()).body(response);
    }
}
