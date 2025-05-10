package com.project.ticketmicroservice.mapper;

import com.project.ticketmicroservice.DTO.FlightBookRequest;
import com.project.ticketmicroservice.DTO.InfoPassengerDTO;
import com.project.ticketmicroservice.DTO.TicketCreateRequest;
import com.project.ticketmicroservice.DTO.TicketFullInfo;
import com.project.ticketmicroservice.entity.InfoPassenger;
import com.project.ticketmicroservice.entity.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketMapper {
    public FlightBookRequest mapTicketCreateToFlightBook(TicketCreateRequest createReq) {
        FlightBookRequest flightReq = new FlightBookRequest();
        flightReq.setId(createReq.getFlightId());
        flightReq.setNumPeople(createReq.getInfoPassengerDTOList().size());
        flightReq.setSeatClass(createReq.getSeatClass());
        return flightReq;
    }

    public TicketFullInfo mapTicketToTicketFullInfo(Ticket ticket) {
        TicketFullInfo ticketFullInfo = new TicketFullInfo();
        ticketFullInfo.setId(ticket.getId());
        ticketFullInfo.setConfirmCode(ticket.getConfirmCode());
        ticketFullInfo.setUserId(ticket.getUserId());
        ticketFullInfo.setFlightId(ticket.getFlightId());
        ticketFullInfo.setPaymentId(ticket.getPaymentId());
        ticketFullInfo.setPrice(ticket.getPrice());
        ticketFullInfo.setSeatClass(ticket.getSeatClass());
        ticketFullInfo.setInfoPassengerDTOList(mapInfoPassListToInfoPassDTOList(ticket.getInfoPassengerList()));
        return ticketFullInfo;
    }

    public InfoPassengerDTO mapInfoPassToInfoPassDTO(InfoPassenger info) {
        InfoPassengerDTO infoDTO = new InfoPassengerDTO();
        infoDTO.setFirstName(info.getFirstName());
        infoDTO.setLastName(info.getLastName());
        infoDTO.setBirthday(info.getBirthday());
        infoDTO.setPassportId(info.getPassportId());
        infoDTO.setPassExpireDate(info.getPassExpireDate());
        return infoDTO;
    }

    public List<InfoPassengerDTO> mapInfoPassListToInfoPassDTOList(List<InfoPassenger> infos) {
        return infos.stream().map(info -> mapInfoPassToInfoPassDTO(info)).toList();
    }

    public List<TicketFullInfo> mapTicketListToTicketFullInfoList(List<Ticket> tickets) {
        return tickets.stream().map(ticket -> mapTicketToTicketFullInfo(ticket)).toList();
    }
}
