package com.project.flightmicroservice.DTO;

import com.project.flightmicroservice.type.SeatClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateBookedSeatsReq(@NotNull(message = "Flight id required") Long flightId,
                                   @Enumerated(EnumType.STRING) SeatClass seatClass,
                                   @Min(value = 1, message = "At least 1 seat to update") int numBookedSeats) {
}
