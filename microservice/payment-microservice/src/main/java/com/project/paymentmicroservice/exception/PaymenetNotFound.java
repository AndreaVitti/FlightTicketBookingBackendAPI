package com.project.paymentmicroservice.exception;

public class PaymenetNotFound extends RuntimeException {
    public PaymenetNotFound(String message) {
        super(message);
    }
}
