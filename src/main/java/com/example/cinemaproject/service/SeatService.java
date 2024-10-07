package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.repository.SeatRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getSeatsBySession(Long sessionId) {
        return seatRepository.findBySessionId(sessionId);
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
    }

    public Seat updateSeat(Seat seat) {
        return seatRepository.save(seat);
    }
}
