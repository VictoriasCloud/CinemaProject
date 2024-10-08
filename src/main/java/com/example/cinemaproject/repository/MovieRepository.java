package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Дополнительный метод поиска фильмов по названию
    Movie findByTitle(String title);

}
