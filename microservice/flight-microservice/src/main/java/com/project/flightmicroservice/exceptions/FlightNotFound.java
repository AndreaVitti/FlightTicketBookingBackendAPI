package com.project.flightmicroservice.exceptions;

public class FlightNotFound extends RuntimeException {
    public FlightNotFound(String message) {
        super(message);
    }
}
