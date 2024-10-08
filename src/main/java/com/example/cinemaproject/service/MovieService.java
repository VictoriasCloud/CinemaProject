package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Movie;
import com.example.cinemaproject.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Получить список всех фильмов
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Получить фильм по ID
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    // Сохранить фильм
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    // Удалить фильм по ID
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    // Найти фильм по названию
    public Movie getMovieByTitle(String title) {
        return movieRepository.findByTitle(title);
    }
}
