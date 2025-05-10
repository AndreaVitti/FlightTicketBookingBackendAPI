package com.project.ticketmicroservice.DTO;

import com.project.ticketmicroservice.type.SeatClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FlightBookRequest {
    @NotNull(message = "Id required")
    private Long id;
    @Min(value = 1, message = "At least one passenger required")
    private int numPeople;
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
}
