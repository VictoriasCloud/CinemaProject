package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Room;
import com.example.cinemaproject.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Получение всех комнат с пагинацией
    @GetMapping
    public ResponseEntity<Page<Room>> getAllRooms(@PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(roomService.getAllRooms(pageable));
    }

    // Получение комнаты по ID
    @GetMapping("/")
    public ResponseEntity<Room> getRoomById(@RequestParam Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Поиск комнаты по имени с пагинацией
    @GetMapping("/search")
    public ResponseEntity<Page<Room>> getRoomsByName(@RequestParam String name, @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(roomService.findRoomsByName(name, pageable));
    }

    // Поиск комнаты по номеру зала
    @GetMapping("/searchByNumber")
    public ResponseEntity<Page<Room>> getRoomsByNumber(@RequestParam int number, @PageableDefault(size = 5) Pageable pageable) {
        return ResponseEntity.ok(roomService.findRoomsByNumber(number, pageable));
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomService.createRoom(room));
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Room>> createRoomsBatch(@RequestBody List<Room> rooms) {
        return ResponseEntity.ok(roomService.createRoomsBatch(rooms));
    }

    @PutMapping("/")
    public ResponseEntity<Room> updateRoom(@RequestParam Long id, @RequestBody Room updatedRoom) {
        return ResponseEntity.ok(roomService.updateRoom(id, updatedRoom));
    }

    @DeleteMapping("/")
    public ResponseEntity<Map<String, String>> deleteRoom(@RequestParam Long id) {
        roomService.deleteRoom(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Room with ID " + id + " has been successfully deleted.");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/all")
    public ResponseEntity<Map<String, String>> deleteAllRooms() {
        roomService.deleteAllRooms();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All rooms have been successfully deleted.");
        return ResponseEntity.ok(response);
    }
}
