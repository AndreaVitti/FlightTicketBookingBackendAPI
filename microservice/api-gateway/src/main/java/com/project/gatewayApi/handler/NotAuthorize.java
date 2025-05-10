package com.project.gatewayApi.handler;

public class NotAuthorize extends RuntimeException {
    public NotAuthorize(String message) {
        super(message);
    }
}
