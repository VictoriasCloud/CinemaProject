package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Movie;
import com.example.cinemaproject.service.MovieService;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // Создание списка фильмов
    @PostMapping("/batch")
    public ResponseEntity<List<Movie>> createMoviesBatch(@RequestBody List<Movie> movies) {
        List<Movie> savedMovies = movieService.saveAll(movies); // сохраняем фильмы и возвращаем список
        return ResponseEntity.ok(savedMovies); // возвращаем список созданных фильмов в виде JSON
    }


    // Обновление списка фильмов
    @PutMapping("/batch")
    public ResponseEntity<String> updateMoviesBatch(@RequestBody List<Movie> movies) {
        movieService.updateAll(movies);
        return ResponseEntity.ok("Movies updated successfully");
    }


    @GetMapping()
    public Page<Movie> getAllMovies(@PageableDefault(size = 5) Pageable pageable) {
        return movieService.getAllMovies(pageable);
    }

    // Удалить все фильмы
    @DeleteMapping
    public ResponseEntity<String> deleteAllMovies() {
        movieService.deleteAllMovies();
        return ResponseEntity.ok("All movies have been deleted successfully.");
    }


    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.saveMovie(movie);
        return ResponseEntity.ok(savedMovie);
    }

    // Обновить фильм
    @PutMapping("/")
    public ResponseEntity<Movie> updateMovie(@RequestParam Long id, @RequestBody Movie updatedMovie) {
        Movie movie = movieService.updateMovie(id, updatedMovie);
        return ResponseEntity.ok(movie);
    }

    @DeleteMapping("/")
    public ResponseEntity<String> deleteMovie(@RequestParam Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.ok("Movie with ID " + id + " has been successfully deleted.");
    }
    @GetMapping("/")
    public ResponseEntity<Movie> getMovieById(@RequestParam Long id) {
        Movie movie = movieService.getMovieById(id);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/search")
    public Page<Movie> getMoviesByTitle(@RequestParam String title, Pageable pageable) {
        return movieService.getMoviesByTitle(title, pageable);
    }

}
