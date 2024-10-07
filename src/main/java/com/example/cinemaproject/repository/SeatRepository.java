package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySessionId(Long sessionId);
}
