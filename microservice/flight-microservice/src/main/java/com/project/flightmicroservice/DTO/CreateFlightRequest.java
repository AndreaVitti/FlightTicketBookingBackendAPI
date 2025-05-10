package com.project.flightmicroservice.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateFlightRequest {
    @NotBlank(message = "Starting Location required")
    private String startLoc;
    @NotBlank(message = "Destination required")
    private String destination;
    @NotBlank(message = "Departure time required")
    @Future(message = "Departime time not valid")
    private LocalDateTime departureTime;
    @NotBlank(message = "Arrival time required")
    @Future(message = "Arrival time not valid")
    private LocalDateTime arrivalTime;
    @Min(value = 1, message = "At least one seat")
    private Integer economySeats;
    @Min(value = 1, message = "At least one seat")
    private Integer businessSeats;
    @Min(value = 1, message = "At least one seat")
    private Integer firstClassSeats;
    @DecimalMin(value = "0.50", message = "Minimal price is 0.50")
    private BigDecimal price;
}
