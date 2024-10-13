package com.example.cinemaproject.dto;

import java.time.LocalDateTime;

public class SessionUpdateDTO {
    private LocalDateTime startTime;
    private Double seatPrice;

    // Геттеры и сеттеры
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Double getSeatPrice() {
        return seatPrice;
    }

    public void setSeatPrice(Double seatPrice) {
        this.seatPrice = seatPrice;
    }
}
