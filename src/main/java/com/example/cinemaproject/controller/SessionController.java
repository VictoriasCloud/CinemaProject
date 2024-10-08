package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        Session session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }

    @PostMapping
    public ResponseEntity<String> createSession(@RequestBody Session session) {
        LocalDateTime startTime = session.getStartTime();
        LocalDateTime endTime = session.getEndTime();
        int hallNumber = session.getHallNumber();

        // Проверяем, свободно ли время для нового сеанса
        if (sessionService.isSessionTimeAvailable(startTime, endTime, hallNumber)) {
            sessionService.saveSession(session);
            return ResponseEntity.ok("Session created successfully");
        } else {
            return ResponseEntity.badRequest().body("Time slot is already booked for another session");
        }
    }
}
