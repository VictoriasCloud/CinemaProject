package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);

    List<Session> findByHallNumber(int hallNumber);
}
