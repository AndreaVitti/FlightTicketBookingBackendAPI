package com.project.ticketmicroservice.repository;

import com.project.ticketmicroservice.entity.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByConfirmCode(String confirmCode);

    Optional<Page<Ticket>> findByUserId(Long userId, Pageable pageable);

    void deleteByConfirmCode(String confirmCode);
}
