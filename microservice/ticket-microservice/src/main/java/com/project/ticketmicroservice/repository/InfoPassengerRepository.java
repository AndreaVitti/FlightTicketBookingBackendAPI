package com.project.ticketmicroservice.repository;

import com.project.ticketmicroservice.entity.InfoPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoPassengerRepository extends JpaRepository<InfoPassenger, Long> {
}
