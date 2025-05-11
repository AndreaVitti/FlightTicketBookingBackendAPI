package com.project.flightmicroservice.exceptions;

public class CantAccessResource extends RuntimeException {
    public CantAccessResource(String message) {
        super(message);
    }
}
