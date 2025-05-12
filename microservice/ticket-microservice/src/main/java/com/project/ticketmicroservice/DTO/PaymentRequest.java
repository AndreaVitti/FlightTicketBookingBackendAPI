package com.project.ticketmicroservice.DTO;

import java.math.BigDecimal;

public record PaymentRequest(String ticketConfirmCode, String currency, BigDecimal price) {
}