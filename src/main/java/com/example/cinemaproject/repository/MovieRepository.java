package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Дополнительный метод поиска фильмов по названию
//    Movie findByTitle(String title);
    List<Movie> findByTitleContainingIgnoreCase(String title);

}
