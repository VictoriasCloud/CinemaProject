package com.example.cinemaproject.dto;

import java.time.LocalDateTime;

public class SessionRequestDTO {
    private Long movieId;
    private Long roomId;
    private LocalDateTime startTime;
    private Double ticketPriceModifier; // Надбавка к базовой цене за место

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

    public Double getTicketPriceModifier() {
        return ticketPriceModifier;
    }

    public void setTicketPriceModifier(Double ticketPriceModifier) {
        this.ticketPriceModifier = ticketPriceModifier;
    }
}
