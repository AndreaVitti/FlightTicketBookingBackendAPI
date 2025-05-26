package com.project.paymentmicroservice.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(@NotNull(message = "Payment id required") Long id,
                         @NotBlank(message = "Ticket confirmation code is required") String ticketConfirmCode,
                         @NotBlank(message = "Currency required") String currency,
                         @DecimalMin(value = "0.5", message = "Minimal price is 0.5") BigDecimal price,
                         @NotNull(message = "Date of the payment required") LocalDateTime creationDate) {
}
