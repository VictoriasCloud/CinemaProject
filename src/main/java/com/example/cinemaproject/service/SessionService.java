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

    public Session createSession(Session session) {
        return sessionRepository.save(session);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    // Проверка на наличие пересечений с другими сеансами в указанном зале
    public boolean isSessionTimeAvailable(LocalDateTime startTime, LocalDateTime endTime, int roomNumber) {
        List<Session> existingSessions = sessionRepository.findByRoomRoomNumber(roomNumber);

        // Проверка пересечения времени
        for (Session existingSession : existingSessions) {
            if (startTime.isBefore(existingSession.getEndTime()) && endTime.isAfter(existingSession.getStartTime())) {
                return false;  // Есть пересечение
            }
        }
        return true;  // Время свободно
    }

    public Session updateSession(Long id, Session sessionDetails) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setMovie(sessionDetails.getMovie());
        session.setRoom(sessionDetails.getRoom());
        session.setStartTime(sessionDetails.getStartTime());
        session.setEndTime(sessionDetails.getEndTime());

        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}
