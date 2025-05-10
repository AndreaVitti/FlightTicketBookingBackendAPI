package com.project.ticketmicroservice.DTO;

import com.project.ticketmicroservice.type.SeatClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class TicketCreateRequest {
    @NotNull(message = "User id required")
    private Long userId;
    @Size(min = 1, message = "At least 1 passenger")
    List<InfoPassengerDTO> infoPassengerDTOList;
    @NotNull(message = "Flight id required")
    private Long flightId;
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
}
