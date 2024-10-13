package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findBySessionIdAndSeatStatus(Long sessionId, String seatStatus);
    List<Seat> findBySessionId(Long sessionId);

    // Метод для удаления всех мест, связанных с конкретной сессией
    @Modifying
    @Transactional
    @Query("DELETE FROM Seat s WHERE s.session.id = :sessionId")
    void deleteBySessionId(@Param("sessionId") Long sessionId);
}
