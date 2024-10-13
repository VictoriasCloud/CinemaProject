package com.example.cinemaproject.dto;

import java.time.LocalDateTime;

public class SessionRequest {

    private Long movieId;
    private Long roomId;
    private LocalDateTime startTime;

    // Геттеры и сеттеры
    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
}
