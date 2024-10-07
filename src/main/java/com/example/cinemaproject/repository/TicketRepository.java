package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
