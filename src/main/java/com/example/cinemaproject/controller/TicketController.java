package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Ticket;
import com.example.cinemaproject.service.SeatService;
import com.example.cinemaproject.service.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final SeatService seatService;

    public TicketController(TicketService ticketService, SeatService seatService) {
        this.ticketService = ticketService;
        this.seatService = seatService;
    }

    @PostMapping("/buy/{seatId}")
    public ResponseEntity<String> buyTicket(@PathVariable Long seatId) {
        Seat seat = seatService.getSeatById(seatId);
        if (seat.getStatus().equals("free")) {
            ticketService.buyTicket(seat);
            return ResponseEntity.ok("Ticket purchased successfully");
        } else {
            return ResponseEntity.badRequest().body("Seat is already taken");
        }
    }

    @PostMapping("/return/{seatId}")
    public ResponseEntity<String> returnTicket(@PathVariable Long seatId) {
        Seat seat = seatService.getSeatById(seatId);
        if (seat.getStatus().equals("taken")) {
            ticketService.returnTicket(seat);
            return ResponseEntity.ok("Ticket returned successfully");
        } else {
            return ResponseEntity.badRequest().body("Seat is not taken");
        }
    }
}
