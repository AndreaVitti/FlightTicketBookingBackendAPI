package com.project.flightmicroservice.repository;

import com.project.flightmicroservice.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    @Query("SELECT f FROM Flight f WHERE " +
            "(CASE WHEN (f.startLoc = ?1 AND f.destination = ?2 AND 'ECONOMY' = ?3 AND f.economySeats >= ?4) THEN 1" +
            "WHEN (f.startLoc = ?1 AND f.destination = ?2 AND 'BUSINESS' = ?3 AND f.businessSeats >= ?4) THEN 2 " +
            "WHEN (f.startLoc = ?1 AND f.destination = ?2 AND 'FIRST_CLASS' = ?3 AND f.firstClassSeats >= ?4) THEN 3 " +
            "ELSE 0 END > 0)")
    Optional<Page<Flight>> findAvailability(String startLoc, String destination, String name, int numPeople, Pageable pageable);

    @Query("SELECT f FROM Flight f WHERE " +
            "(CASE WHEN (f.id = ?1 AND 'ECONOMY' = ?2 AND f.economySeats >= ?3) THEN 1" +
            "WHEN (f.id = ?1 AND 'BUSINESS' = ?2 AND f.businessSeats >= ?3) THEN 2 " +
            "WHEN (f.id = ?1 AND 'FIRST_CLASS' = ?2 AND f.firstClassSeats >= ?3) THEN 3 " +
            "ELSE 0 END > 0)")
    Optional<Flight> findIfSeatsEmpty(Long id, String name, int numPeople);
}
