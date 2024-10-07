package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.repository.SessionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getSessionsByDate(LocalDate date) {
        return sessionRepository.findByStartTimeBetween(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }
}
