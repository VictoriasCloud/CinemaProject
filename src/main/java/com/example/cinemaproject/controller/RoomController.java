package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.Room;
import com.example.cinemaproject.service.RoomService;
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
    // Получение всех комнат
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    // Получение комнаты по ID
    //если не разделю пути и оставлю только раазницу в параметрах-ошибка
    @GetMapping("/")
    public ResponseEntity<Room> getRoomById(@RequestParam Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Поиск комнаты по имени
    @GetMapping("/search/")
    public ResponseEntity<List<Room>> getRoomsByName(@RequestParam String name) {
        return ResponseEntity.ok(roomService.findRoomsByName(name));
    }

    // Поиск комнаты по номеру зала
    @GetMapping("/searchByNumber/")
    public ResponseEntity<List<Room>> getRoomsByNumber(@RequestParam int number) {
        return ResponseEntity.ok(roomService.findRoomsByNumber(number));
    }
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomService.createRoom(room));
    }

    // Добавление списка комнат
    @PostMapping("/batch")
    public ResponseEntity<List<Room>> createRoomsBatch(@RequestBody List<Room> rooms) {
        return ResponseEntity.ok(roomService.createRoomsBatch(rooms));
    }

    // Обновление комнаты по ID
    @PutMapping("/")
    public ResponseEntity<Room> updateRoom(@RequestParam Long id, @RequestBody Room updatedRoom) {
        return ResponseEntity.ok(roomService.updateRoom(id, updatedRoom));
    }

    // Удалить комнату по id и вернуть сообщение
    @DeleteMapping("/")
    public ResponseEntity<Map<String, String>> deleteRoom(@RequestParam Long id) {
        roomService.deleteRoom(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Room with ID " + id + " has been successfully deleted.");
        return ResponseEntity.ok(response);
    }

    // Удаление всех комнат
    @DeleteMapping()
    public ResponseEntity<Map<String, String>> deleteAllRooms() {
        roomService.deleteAllRooms();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All rooms have been successfully deleted.");
        return ResponseEntity.ok(response);
    }
}
