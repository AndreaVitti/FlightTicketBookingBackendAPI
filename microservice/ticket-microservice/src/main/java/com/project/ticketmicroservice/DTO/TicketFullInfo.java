package com.project.ticketmicroservice.DTO;

import com.project.ticketmicroservice.type.SeatClass;

import java.math.BigDecimal;
import java.util.List;

public record TicketFullInfo(Long id,
                             String confirmCod,
                             Long userId,
                             Long flightId,
                             Long paymentId,
                             BigDecimal price,
                             SeatClass seatClass,
                             List<InfoPassengerDTO> infoPassengerDTOList) {
}
