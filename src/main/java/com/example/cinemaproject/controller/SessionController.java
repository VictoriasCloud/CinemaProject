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

    @GetMapping("/")
    public ResponseEntity<Session> getSessionById(@RequestParam Long id) {
        Session session = sessionService.getSessionById(id);
        return ResponseEntity.ok(session);
    }

    @PostMapping
    public ResponseEntity<Session> createSession(@RequestParam Long movieId,
                                                 @RequestParam Long roomId,
                                                 @RequestParam LocalDateTime startTime) {
        Session session = sessionService.createSession(movieId, roomId, startTime);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/")
    public ResponseEntity<Session> updateSession(@RequestParam Long id, @RequestBody Session sessionDetails) {
        return ResponseEntity.ok(sessionService.updateSession(id, sessionDetails));
    }

    @DeleteMapping("/")
    public ResponseEntity<Void> deleteSession(@RequestParam Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
