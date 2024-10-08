package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public boolean isSessionTimeAvailable(LocalDateTime startTime, LocalDateTime endTime, int hallNumber) {
        List<Session> sessions = sessionRepository.findByHallNumber(hallNumber);
        for (Session s : sessions) {
            if (startTime.isBefore(s.getEndTime()) && endTime.isAfter(s.getStartTime())) {
                return false;
            }
        }
        return true;
    }
}
