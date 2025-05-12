package com.project.flightmicroservice.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponse(int httpCode, Long id, String startLoc, String destination, LocalDateTime departureTime,
                              LocalDateTime arrivalTime, BigDecimal ticketPrice) {
}
