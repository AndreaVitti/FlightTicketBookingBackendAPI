package com.project.paymentmicroservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutComplReq(@NotBlank(message = "Ticket confirmation code required") String ticketConfirmationCode,
                               @NotNull(message = "Payment id required")Long paymentId) {
}
