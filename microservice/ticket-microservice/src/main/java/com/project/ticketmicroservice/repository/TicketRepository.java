package com.project.ticketmicroservice.repository;

import com.project.ticketmicroservice.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByConfirmCode(String confirmCode);

    List<Ticket> findByUserId(Long userId);

    void deleteByConfirmCode(String confirmCode);
}
