package com.project.ticketmicroservice.exception;

public class CantAccessResource extends RuntimeException {
    public CantAccessResource(String message) {
        super(message);
    }
}
