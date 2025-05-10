package com.project.flightmicroservice.DTO;

import com.project.flightmicroservice.type.SeatClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookingRequest {
    @NotNull(message = "Id required")
    private Long id;
    @Min(value = 1, message = "At least one passenger required")
    private int numPeople;
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
}
