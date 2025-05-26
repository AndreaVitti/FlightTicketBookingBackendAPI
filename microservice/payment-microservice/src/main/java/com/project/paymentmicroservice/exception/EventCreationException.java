package com.project.paymentmicroservice.exception;

public class EventCreationException extends RuntimeException {
    public EventCreationException(String message) {
        super(message);
    }
}
