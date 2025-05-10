package com.project.flightmicroservice.DTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private Long id;
    private String startLoc;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Integer economySeats;
    private Integer businessSeats;
    private Integer firstClassSeats;
    private BigDecimal price;
}
