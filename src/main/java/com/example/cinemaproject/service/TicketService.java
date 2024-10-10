package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Ticket;
import com.example.cinemaproject.model.User;
import com.example.cinemaproject.repository.SeatRepository;
import com.example.cinemaproject.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;

    public TicketService(TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    // Покупка билета
    public void buyTicket(Seat seat, double seatPrice, String seatNumber, User user) {
        if (!seat.getSeatStatus().equals("free")) {
            throw new RuntimeException("Seat is already taken");
        }

        Ticket ticket = new Ticket();
        ticket.setSeat(seat);
        ticket.setStatus("bought");
        ticket.setSeatPrice(seatPrice);  // Устанавливаем цену
        ticket.setSeatNumber(seatNumber);  // Устанавливаем номер места
        ticket.setUser(user);  // Сохраняем информацию о пользователе
        ticket.setSession(seat.getSession());  // Устанавливаем сеанс

        seat.setSeatStatus("taken");  // Обновляем статус места
        seatRepository.save(seat);  // Сохраняем изменения места
        ticketRepository.save(ticket);  // Сохраняем билет
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

    public List<Ticket> findTicketsByUserId(Long userId) {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> findTicketsByUserEmail(String email) {
        return ticketRepository.findByUserEmail(email);
    }

    public List<Ticket> findTicketsByUserName(String name) {
        return ticketRepository.findByUserName(name);
    }

    public Ticket findTicketBySeatAndSession(String seatNumber, Long sessionId) {
        return ticketRepository.findBySeatNumberAndSessionId(seatNumber, sessionId);
    }

    public List<Ticket> findTicketsBySessionId(Long sessionId) {
        return ticketRepository.findBySessionId(sessionId);
    }


}
