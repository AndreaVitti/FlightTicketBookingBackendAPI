package com.project.ticketmicroservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutCompletedRequest(
        @NotBlank(message = "Ticket confirmation code required") String ticketConfirmationCode,
        @NotNull(message = "Payment id required") Long paymentId) {
}
