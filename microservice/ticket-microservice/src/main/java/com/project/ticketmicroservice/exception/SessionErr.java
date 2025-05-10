package com.project.ticketmicroservice.exception;

public class SessionErr extends RuntimeException {
    public SessionErr(String message) {
        super(message);
    }
}
