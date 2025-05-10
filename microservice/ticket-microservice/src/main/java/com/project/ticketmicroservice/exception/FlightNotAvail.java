package com.project.ticketmicroservice.exception;

public class FlightNotAvail extends RuntimeException {
    public FlightNotAvail(String message) {
        super(message);
    }
}
