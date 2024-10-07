package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Ticket;
import com.example.cinemaproject.service.TicketService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/buy")
    public Ticket buyTicket(@RequestParam Long seatId,
                            @RequestParam Long sessionId,
                            @RequestParam String buyerName,
                            @RequestParam String buyerEmail) {
        return ticketService.buyTicket(seatId, sessionId, buyerName, buyerEmail);
    }
}
