package com.project.flightmicroservice.exceptions;

public class ArrivalTimeInvalid extends RuntimeException {
    public ArrivalTimeInvalid(String message) {
        super(message);
    }
}
