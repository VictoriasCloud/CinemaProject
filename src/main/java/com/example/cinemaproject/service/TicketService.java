package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Ticket;
import com.example.cinemaproject.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatService seatService;
    private final SessionService sessionService;

    public TicketService(TicketRepository ticketRepository, SeatService seatService, SessionService sessionService) {
        this.ticketRepository = ticketRepository;
        this.seatService = seatService;
        this.sessionService = sessionService;
    }

    public Ticket buyTicket(Long seatId, Long sessionId, String buyerName, String buyerEmail) {
        Ticket ticket = new Ticket();
        ticket.setSeat(seatService.getSeatById(seatId));
        ticket.setSession(sessionService.getSessionById(sessionId));
        ticket.setBuyerName(buyerName);
        ticket.setBuyerEmail(buyerEmail);
        return ticketRepository.save(ticket);
    }
}
