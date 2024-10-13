package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
    List<Ticket> findByUserFullName(String fullName);
    List<Ticket> findByTicketStatus(Ticket.TicketStatus status);
    List<Ticket> findBySessionId(Long sessionId);
}
