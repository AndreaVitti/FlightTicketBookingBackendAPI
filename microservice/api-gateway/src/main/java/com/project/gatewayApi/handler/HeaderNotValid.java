package com.project.gatewayApi.handler;

public class HeaderNotValid extends RuntimeException {
    public HeaderNotValid(String message) {
        super(message);
    }
}
