package com.project.paymentmicroservice.exception;

public class ServerErr extends RuntimeException {
    public ServerErr(String message) {
        super(message);
    }
}
