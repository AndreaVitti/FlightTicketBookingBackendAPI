package com.project.ticketmicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    private int httpCode;
    private String message;

    private Long paymentId;

    private String sessionId;
    private String sessionUrl;
}
