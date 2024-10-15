package com.example.cinemaproject.controller;

import com.example.cinemaproject.dto.SessionRequestDTO;
import com.example.cinemaproject.dto.SessionUpdateDTO;
import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.service.KafkaProducerService;
import com.example.cinemaproject.service.SessionSchedulerService;
import com.example.cinemaproject.service.SessionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;
    private final KafkaProducerService kafkaProducerService;

    public SessionController(SessionService sessionService, KafkaProducerService kafkaProducerService) {
        this.sessionService = sessionService;
        this.kafkaProducerService = kafkaProducerService;
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
    public ResponseEntity<Session> createSession(@RequestBody SessionRequestDTO sessionRequestDTO, SessionSchedulerService sessionSchedulerService) {
        // Извлечение данных из объекта sessionRequest
        Session session = sessionService.createSession(sessionRequestDTO);
        return ResponseEntity.ok(session);
    }

    @PutMapping("/")
    public ResponseEntity<Session> updateSession(@RequestParam Long id, @RequestBody SessionUpdateDTO sessionUpdateDTO) {
        Session updatedSession = sessionService.updateSession(id, sessionUpdateDTO);
        return ResponseEntity.ok(updatedSession);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteSession(@RequestParam Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok("Session with ID " + id + " has been successfully deleted.");
    }

    // Удаление всех сессий
    @DeleteMapping
    public ResponseEntity<String> deleteAllSessions() {
        sessionService.deleteAllSessions();
        return ResponseEntity.ok("All sessions have been deleted.");
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<Session>> getSessionsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<Session> sessions = sessionService.getSessionsByDate(date);
        return ResponseEntity.ok(sessions);
    }

}
