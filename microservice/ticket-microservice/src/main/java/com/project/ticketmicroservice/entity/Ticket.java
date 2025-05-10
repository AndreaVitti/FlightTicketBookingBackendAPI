package com.project.ticketmicroservice.entity;

import com.project.ticketmicroservice.type.SeatClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String confirmCode;
    private Long userId;
    private Long flightId;
    private Long paymentId;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;
    @OneToMany(mappedBy = "ticket", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<InfoPassenger> infoPassengerList;
}
