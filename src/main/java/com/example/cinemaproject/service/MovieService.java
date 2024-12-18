package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Movie;
import com.example.cinemaproject.repository.MovieRepository;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Получить список всех фильмов с пагинацией
    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    // Получить фильм по ID
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    // Сохранить фильм
    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie movie = getMovieById(id);
        movie.setTitle(updatedMovie.getTitle());
        movie.setDuration(updatedMovie.getDuration());
        movie.setDescription(updatedMovie.getDescription());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
        movieRepository.delete(movie);
    }


    public List<Movie> saveAll(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    public void updateAll(List<Movie> movies) {
        movieRepository.saveAll(movies);
    }
    public void deleteAllMovies() {
        movieRepository.deleteAll();
    }

    // Поиск фильмов по названию с пагинацией
    public Page<Movie> getMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }
}
