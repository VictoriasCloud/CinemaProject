package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

//Репозиторий отвечает за выполнение операций с базой данных (CRUD — создание, чтение, обновление, удаление).
public interface MovieRepository extends JpaRepository<Movie, Long> {
    // Репозиторий для работы с фильмами
}
