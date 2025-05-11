package com.project.ticketmicroservice.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    String ticketConfirmCode;
    String currency;
    BigDecimal price;
}