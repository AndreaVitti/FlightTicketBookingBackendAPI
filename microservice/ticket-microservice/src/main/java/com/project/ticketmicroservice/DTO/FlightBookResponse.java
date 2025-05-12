package com.project.ticketmicroservice.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FlightBookResponse(Long id,
                                 String startLoc,
                                 String destination,
                                 LocalDateTime departureTime,
                                 LocalDateTime arrivalTime,
                                 BigDecimal ticketPrice) {
}
