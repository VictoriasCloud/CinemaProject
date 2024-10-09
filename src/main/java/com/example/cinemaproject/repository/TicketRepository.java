package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Найти все билеты для конкретного сеанса
    List<Ticket> findBySessionId(Long sessionId);

    // Найти все билеты для конкретного пользователя (например, по email)
    List<Ticket> findByUserEmail(String email);

    // Найти билет по месту и сеансу
    Ticket findBySeatIdAndSessionId(Long seatId, Long sessionId);
    //найти билет по юзерId
//    List<Ticket> findByUserId(Long userId);


    // Найти билет по месту
    Ticket findBySeat(Seat seat);
}
