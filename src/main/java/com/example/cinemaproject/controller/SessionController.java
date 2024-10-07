package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.service.SessionService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    // Получение списка сеансов на конкретную дату
    @GetMapping("/by-date")
    public List<Session> getSessionsByDate(@RequestParam("date") String date) {
        return sessionService.getSessionsByDate(LocalDate.parse(date));
    }

    // Получение информации о конкретном сеансе
    @GetMapping("/{id}")
    public Session getSessionById(@PathVariable Long id) {
        return sessionService.getSessionById(id);
    }

    // Добавление нового сеанса
    @PostMapping
    public Session createSession(@RequestBody Session session) {
        return sessionService.createSession(session);
    }
}
