package com.project.ticketmicroservice.DTO;

import com.project.ticketmicroservice.type.SeatClass;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class TicketFullInfo {
    private Long id;
    private String confirmCode;
    private Long userId;
    private Long flightId;
    private Long paymentId;
    private BigDecimal price;
    private SeatClass seatClass;
    private List<InfoPassengerDTO> infoPassengerDTOList;
}
