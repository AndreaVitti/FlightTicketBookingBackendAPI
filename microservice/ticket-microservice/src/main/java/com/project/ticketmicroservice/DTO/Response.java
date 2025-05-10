package com.project.ticketmicroservice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int httpCode;
    private String message;

    private Long ticketId;
    private String confirmationCode;
    private TicketFullInfo ticketFullInfo;
    private List<TicketFullInfo> ticketFullInfoList;

    private Long paymentId;
    private String sessionId;
    private String sessionUrl;
}
