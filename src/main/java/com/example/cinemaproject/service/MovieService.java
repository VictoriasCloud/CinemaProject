package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Movie;
import com.example.cinemaproject.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


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


    // Сохранение списка фильмов (batch create)
// Сохранение списка фильмов (batch create)
    public List<Movie> saveAll(List<Movie> movies) {
        return movieRepository.saveAll(movies);
    }

    // не помню работает ли надо проверитьОбновление списка фильмов (batch update)
    public void updateAll(List<Movie> movies) {
        movieRepository.saveAll(movies); // saveAll в JPA поддерживает как создание, так и обновление
    }

    // Удаление всех фильмов
    public void deleteAllMovies() {
        movieRepository.deleteAll();
    }


//    // Найти фильм по названию
//    public Movie getMovieByTitle(String title) {
//        return movieRepository.findByTitle(title);
//    }

    public List<Movie> getMoviesByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }
}
