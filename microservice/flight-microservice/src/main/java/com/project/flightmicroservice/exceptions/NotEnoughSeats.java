package com.project.flightmicroservice.exceptions;

public class NotEnoughSeats extends RuntimeException {
    public NotEnoughSeats(String message) {
        super(message);
    }
}
