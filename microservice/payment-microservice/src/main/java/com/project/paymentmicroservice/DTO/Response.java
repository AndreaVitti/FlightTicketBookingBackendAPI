package com.project.paymentmicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int httpCode;
    private String message;

    private Long paymentId;

    private String sessionId;
    private String sessionUrl;
    private PaymentDTO paymentDTO;
    private List<PaymentDTO> paymentDTOList;
}
