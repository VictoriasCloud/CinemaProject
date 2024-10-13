package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public void createSeatsForSession(Session session) {
        int hallCapacity = 50; // допустим, зал рассчитан на 50 мест
        for (int i = 1; i <= hallCapacity; i++) {
            Seat seat = new Seat();
            seat.setSession(session);
            seat.setSeatNumber(String.valueOf(i));
            seat.setSeatStatus(Seat.SeatStatus.valueOf("free"));
            seatRepository.save(seat);
        }
    }

    public List<Seat> getAvailableSeats(Long sessionId) {
        return seatRepository.findBySessionIdAndSeatStatus(sessionId, "free");
    }

    public Seat getSeatById(Long seatId) {
        return seatRepository.findById(seatId).orElseThrow(() -> new RuntimeException("Seat not found"));
    }
}
