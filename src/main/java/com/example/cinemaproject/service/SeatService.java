package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.repository.SeatRepository;
import com.example.cinemaproject.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final SessionRepository sessionRepository;

    public SeatService(SeatRepository seatRepository, SessionRepository sessionRepository) {
        this.seatRepository = seatRepository;
        this.sessionRepository = sessionRepository;
    }

    // Создать место привязанное к сессии, из сессии заберём рум айди
    public Seat createSeat(Seat seat, Long sessionId) {
        // Проверка на существование сессии
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session with ID " + sessionId + " not found."));

        // Проверка на уникальность seatNumber в рамках конкретной сессии
        if (seatRepository.existsBySessionIdAndSeatNumber(sessionId, seat.getSeatNumber())) {
            throw new RuntimeException("Seat with number " + seat.getSeatNumber() + " already exists in session with ID " + sessionId);
        }

        // Привязываем место к сессии и к залу
        seat.setSession(session);
        seat.setRoom(session.getRoom());

        return seatRepository.save(seat);
    }


    // Получить место по ID
    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    public Seat updateSeat(Long id, Seat updatedSeat) {
        Seat seat = getSeatById(id);
        seat.setSeatNumber(updatedSeat.getSeatNumber());
        seat.setSeatPrice(updatedSeat.getSeatPrice());
        seat.setSeatStatus(updatedSeat.getSeatStatus());
        return seatRepository.save(seat);
    }

    public void deleteSeat(Long id) {
        Seat seat = getSeatById(id);

        if (seat.getSeatStatus() == Seat.SeatStatus.OCCUPIED) {
            throw new RuntimeException("Cannot delete seat with ID " + id + " because it is occupied.");
        }

        seatRepository.delete(seat);
    }


    //все места для определенной сессии
    public List<Seat> getSeatsBySessionId(Long sessionId) {
        return seatRepository.findBySessionId(sessionId);
    }

    //Все места
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }
}
