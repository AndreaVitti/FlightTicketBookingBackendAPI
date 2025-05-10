package com.project.gatewayApi.DTO;

import lombok.Data;

@Data
public class Response {
    private int httpCode;
    private String message;
}
