package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.service.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Получение всех мест для конкретного сеанса
    @GetMapping("/by-session/{sessionId}")
    public List<Seat> getSeatsBySession(@PathVariable Long sessionId) {
        return seatService.getSeatsBySession(sessionId);
    }

    // Получение информации о конкретном месте
    @GetMapping("/{id}")
    public Seat getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    // Обновление статуса места
    @PutMapping("/{id}")
    public Seat updateSeat(@PathVariable Long id, @RequestBody Seat seat) {
        seat.setId(id);
        return seatService.updateSeat(seat);
    }
}
