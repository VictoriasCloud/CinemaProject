package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    //почему с findByRoomNumber ошибка а findByRoomRoomNumber нет ошибки я в шоке
    //Когда вы используете findByRoomRoomNumber, Spring Data JPA понимает, что сначала нужно обратиться
    // к полю room в сущности Session, а затем к полю roomNumber в сущности Room. То есть findByRoomRoomNumber — это путь,
    // который Spring Data JPA использует для построения. Spring Data JPA использует имена методов для автоматического создания SQL-запросов.
    // Для этого он должен знать путь к полям, чтобы построить корректный запрос.
    List<Session> findByRoomRoomNumber(int roomNumber);
}
