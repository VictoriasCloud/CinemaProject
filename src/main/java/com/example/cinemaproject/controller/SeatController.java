package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.service.SeatService;
import com.example.cinemaproject.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;
    private final SessionService sessionService;

    public SeatController(SeatService seatService, SessionService sessionService) {
        this.seatService = seatService;
        this.sessionService = sessionService;
    }

    @PostMapping("/create/{sessionId}")
    public ResponseEntity<String> createSeatsForSession(@PathVariable Long sessionId) {
        Session session = sessionService.getSessionById(sessionId);
        seatService.createSeatsForSession(session);
        return ResponseEntity.ok("Seats created successfully for session " + sessionId);
    }

    @GetMapping("/available/{sessionId}")
    public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable Long sessionId) {
        List<Seat> availableSeats = seatService.getAvailableSeats(sessionId);
        return ResponseEntity.ok(availableSeats);
    }
}
