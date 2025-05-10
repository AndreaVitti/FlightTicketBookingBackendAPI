package com.project.ticketmicroservice.exception;

public class ServerErr extends RuntimeException {
    public ServerErr(String message) {
        super(message);
    }
}
