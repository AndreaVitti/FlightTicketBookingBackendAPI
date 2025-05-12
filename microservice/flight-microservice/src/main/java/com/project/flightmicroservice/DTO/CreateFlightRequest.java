package com.project.flightmicroservice.DTO;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateFlightRequest(@NotBlank(message = "Starting Location required") String startLoc,
                                  @NotBlank(message = "Destination required") String destination,
                                  @NotNull(message = "Departure time required")
                                  @Future(message = "Departime time not valid")
                                  LocalDateTime departureTime,
                                  @NotNull(message = "Arrival time required")
                                  @Future(message = "Arrival time not valid")
                                  LocalDateTime arrivalTime,
                                  @Min(value = 1, message = "At least one seat")
                                  Integer economySeats,
                                  @Min(value = 1, message = "At least one seat")
                                  Integer businessSeats,
                                  @Min(value = 1, message = "At least one seat")
                                  Integer firstClassSeats,
                                  @DecimalMin(value = "0.50", message = "Minimal price is 0.50")
                                  BigDecimal price) {
}
