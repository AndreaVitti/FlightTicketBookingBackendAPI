package com.project.flightmicroservice.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightDTO(Long id, String startLoc, String destination, LocalDateTime departureTime,
                        LocalDateTime arrivalTime, Integer economySeats, Integer businessSeats, Integer firstClassSeats,
                        BigDecimal price) {
}
