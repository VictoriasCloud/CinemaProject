package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.service.SeatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    // Создать место
    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat, @RequestParam Long sessionId) {
        Seat createdSeat = seatService.createSeat(seat, sessionId);
        return ResponseEntity.ok(createdSeat);
    }

    // Получить место по ID
    @GetMapping(params = "id")
    public ResponseEntity<Seat> getSeatById(@RequestParam Long id) {
        Seat seat = seatService.getSeatById(id);
        return ResponseEntity.ok(seat);
    }

    // Обновить место
    @PutMapping
    public ResponseEntity<Seat> updateSeat(@RequestParam Long id, @RequestBody Seat updatedSeat) {
        Seat seat = seatService.updateSeat(id, updatedSeat);
        return ResponseEntity.ok(seat);
    }

    // Удалить место
    @DeleteMapping
    public ResponseEntity<String> deleteSeat(@RequestParam Long id) {
        seatService.deleteSeat(id);
        return ResponseEntity.ok("Seat with ID " + id + " has been successfully deleted.");
    }

    // Получить все места для определённой сессии
    @GetMapping("/session")
    public ResponseEntity<List<Seat>> getSeatsBySessionId(@RequestParam Long id) {
        List<Seat> seats = seatService.getSeatsBySessionId(id);
        return ResponseEntity.ok(seats);
    }

    // Получить все места
    @GetMapping
    public ResponseEntity<List<Seat>> getAllSeats() {
        List<Seat> seats = seatService.getAllSeats();
        return ResponseEntity.ok(seats);
    }
}
