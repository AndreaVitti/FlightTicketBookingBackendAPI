package com.project.flightmicroservice.DTO;

import com.project.flightmicroservice.type.SeatClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(@NotNull(message = "Id required") Long id,
                             @Min(value = 1, message = "At least one passenger required") int numPeople,
                             @Enumerated(EnumType.STRING) SeatClass seatClass) {
}
