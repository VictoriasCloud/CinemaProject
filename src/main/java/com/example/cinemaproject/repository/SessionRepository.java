package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    //почему с findByRoomNumber ошибка а findByRoomRoomNumber нет ошибки я в шоке
    //Когда вы используете findByRoomRoomNumber, Spring Data JPA понимает, что сначала нужно обратиться
    // к полю room в сущности Session, а затем к полю roomNumber в сущности Room. То есть findByRoomRoomNumber — это путь,
    // который Spring Data JPA использует для построения. Spring Data JPA использует имена методов для автоматического создания SQL-запросов.
    // Для этого он должен знать путь к полям, чтобы построить корректный запрос.

    @Query("SELECT COUNT(s) > 0 FROM Session s WHERE s.room.id = :roomId AND ((:startTime BETWEEN s.startTime AND s.endTime) OR (:endTime BETWEEN s.startTime AND s.endTime))")
    boolean isRoomOccupied(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Session s WHERE s.room.id = :roomId AND ((:startTime BETWEEN s.startTime AND s.endTime) OR (:endTime BETWEEN s.startTime AND s.endTime))")
    Session getSessionByRoomAndTime(@Param("roomId") Long roomId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT s FROM Session s WHERE DATE(s.startTime) = :date")
    List<Session> findAllByDate(@Param("date") LocalDate date);
}
