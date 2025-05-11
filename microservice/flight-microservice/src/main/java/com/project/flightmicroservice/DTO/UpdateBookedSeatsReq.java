package com.project.flightmicroservice.DTO;

import com.project.flightmicroservice.type.SeatClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBookedSeatsReq {
    @NotNull(message = "Flight id required")
    private Long flightId;
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
    @Min(value = 1, message = "At least 1 seat to update")
    private int numBookedSeats;
}
