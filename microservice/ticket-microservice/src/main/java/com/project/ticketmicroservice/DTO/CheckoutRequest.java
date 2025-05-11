package com.project.ticketmicroservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CheckoutRequest {
    @NotBlank(message = "Ticket confirm code required")
    String ticketConfirmCode;
    @NotBlank(message = "Currency type required")
    String currency;
}
