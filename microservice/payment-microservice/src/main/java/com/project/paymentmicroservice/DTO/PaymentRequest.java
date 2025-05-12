package com.project.paymentmicroservice.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record PaymentRequest(@NotBlank(message = "Ticket confirm code required")
                             String ticketConfirmCode,
                             @NotBlank(message = "Currency type required")
                             String currency,
                             @DecimalMin(value = "0.50", message = "Minimum value is 0.50")
                             BigDecimal price) {
}
