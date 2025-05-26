package com.project.paymentmicroservice.exception;

public class BadTicketRequest extends RuntimeException {
    public BadTicketRequest(String message) {
        super(message);
    }
}
