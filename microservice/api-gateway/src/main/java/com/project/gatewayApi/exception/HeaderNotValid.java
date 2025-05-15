package com.project.gatewayApi.exception;

public class HeaderNotValid extends RuntimeException {
    public HeaderNotValid(String message) {
        super(message);
    }
}
