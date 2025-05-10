package com.project.paymentmicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int httpCode;
    private String message;

    private Long paymentId;

    private String sessionId;
    private String sessionUrl;
}
