package com.project.gatewayApi.exception;

public class NotAuthorize extends RuntimeException {
    public NotAuthorize(String message) {
        super(message);
    }
}
