package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.User;
import com.example.cinemaproject.model.Ticket;

import com.example.cinemaproject.service.SeatService;
import com.example.cinemaproject.service.TicketService;
import com.example.cinemaproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final SeatService seatService;
    private final UserService userService;

    public TicketController(TicketService ticketService, SeatService seatService, UserService userService) {
        this.ticketService = ticketService;
        this.seatService = seatService;
        this.userService = userService;
    }

    // Покупка билета на конкретное место
    @PostMapping("/buy/{seatId}")
    public ResponseEntity<String> buyTicket(@PathVariable Long seatId,
                                            @RequestParam double seatPrice,
                                            @RequestParam Long userId) {
        Seat seat = seatService.getSeatById(seatId);
        User user = userService.getUserById(userId);

        if (seat.getSeatStatus().equals("free")) {
            ticketService.buyTicket(seat, seatPrice, seat.getSeatNumber(), user);
            return ResponseEntity.ok("Ticket purchased successfully");
        } else {
            return ResponseEntity.badRequest().body("Seat is already taken");
        }
    }

    // Возврат билета
    @PostMapping("/return/{seatId}")
    public ResponseEntity<String> returnTicket(@PathVariable Long seatId) {
        Seat seat = seatService.getSeatById(seatId);
        if (seat.getSeatStatus().equals("taken")) {
            ticketService.returnTicket(seat);
            return ResponseEntity.ok("Ticket returned successfully");
        } else {
            return ResponseEntity.badRequest().body("Seat is not taken");
        }
    }

    // Найти все билеты для пользователя по ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getTicketsByUserId(@PathVariable Long userId) {
        List<Ticket> tickets = ticketService.findTicketsByUserId(userId);
        return ResponseEntity.ok(tickets);
    }

    // Найти все билеты для пользователя по email
    @GetMapping("/user/email/{email}")
    public ResponseEntity<List<Ticket>> getTicketsByUserEmail(@PathVariable String email) {
        List<Ticket> tickets = ticketService.findTicketsByUserEmail(email);
        return ResponseEntity.ok(tickets);
    }

    // Найти все билеты для пользователя по имени
    @GetMapping("/user/name/{name}")
    public ResponseEntity<List<Ticket>> getTicketsByUserName(@PathVariable String name) {
        List<Ticket> tickets = ticketService.findTicketsByUserName(name);
        return ResponseEntity.ok(tickets);
    }

    // Найти билет по месту и сеансу
    @GetMapping("/seat/{seatNumber}/session/{sessionId}")
    public ResponseEntity<Ticket> getTicketBySeatAndSession(@PathVariable String seatNumber, @PathVariable Long sessionId) {
        Ticket ticket = ticketService.findTicketBySeatAndSession(seatNumber, sessionId);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Найти все билеты на конкретный сеанс
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Ticket>> getTicketsBySession(@PathVariable Long sessionId) {
        List<Ticket> tickets = ticketService.findTicketsBySessionId(sessionId);
        return ResponseEntity.ok(tickets);
    }
}
