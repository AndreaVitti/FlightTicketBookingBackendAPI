package com.project.ticketmicroservice.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

public record InfoPassengerDTO(@NotBlank(message = "First name required")
                               String firstName,
                               @NotBlank(message = "Last name required")
                               String lastName,
                               @Past(message = "Invalid birthday date")
                               LocalDate birthday,
                               @NotBlank(message = "Passport id required")
                               String passportId,
                               @Future(message = "Passport expired")
                               LocalDate passExpireDate) {
}
