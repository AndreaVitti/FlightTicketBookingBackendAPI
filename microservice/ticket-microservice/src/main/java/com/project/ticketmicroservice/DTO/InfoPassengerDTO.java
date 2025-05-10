package com.project.ticketmicroservice.DTO;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InfoPassengerDTO {
    @NotBlank(message = "First name required")
    private String firstName;
    @NotBlank(message = "Last name required")
    private String lastName;
    @Past(message = "Invalid birthday date")
    private LocalDate birthday;
    @NotBlank(message = "Passport id required")
    private String passportId;
    @Future(message = "Passport expired")
    private LocalDate passExpireDate;
}
