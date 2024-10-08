package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Ticket;
import com.example.cinemaproject.repository.SeatRepository;
import com.example.cinemaproject.repository.TicketRepository;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public TicketService(TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    public void buyTicket(Seat seat) {
        Ticket ticket = new Ticket();
        ticket.setSeat(seat);
        ticket.setStatus("bought");

        seat.setSeatStatus("taken");
        seatRepository.save(seat);
        ticketRepository.save(ticket);
    }

    public void returnTicket(Seat seat) {
        Ticket ticket = ticketRepository.findBySeat(seat);
        if (ticket != null) {
            ticket.setStatus("returned");
            seat.setSeatStatus("free");

            seatRepository.save(seat);
            ticketRepository.save(ticket);
        }
    }
}
