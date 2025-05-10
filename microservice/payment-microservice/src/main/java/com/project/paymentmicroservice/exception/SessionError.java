package com.project.paymentmicroservice.exception;

public class SessionError extends RuntimeException {
    public SessionError(String message) {
        super(message);
    }
}
