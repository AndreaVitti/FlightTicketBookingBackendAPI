package com.project.ticketmicroservice.DTO;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(@NotBlank(message = "Ticket confirm code required")
                              String ticketConfirmCode,
                              @NotBlank(message = "Currency type required")
                              String currency) {
}
