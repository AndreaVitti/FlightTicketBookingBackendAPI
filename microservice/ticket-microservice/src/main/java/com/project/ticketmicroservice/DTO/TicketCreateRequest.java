package com.project.ticketmicroservice.DTO;

import com.project.ticketmicroservice.type.SeatClass;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TicketCreateRequest (@NotNull(message = "User id required")
                                   Long userId,
                                   @Size(min = 1, message = "At least 1 passenger")
                                   List<InfoPassengerDTO> infoPassengerDTOList,
                                   @NotNull(message = "Flight id required")
                                   Long flightId,
                                   @Enumerated(EnumType.STRING)
                                   SeatClass seatClass) {
}
