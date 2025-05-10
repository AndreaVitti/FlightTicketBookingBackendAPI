package com.project.paymentmicroservice.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotBlank(message = "Ticket confirm code required")
    private String ticketConfirmCode;
    @NotBlank(message = "Currency type required")
    private String currency;
    @DecimalMin(value = "0.50", message = "Minimum value is 0.50")
    private BigDecimal price;
}
