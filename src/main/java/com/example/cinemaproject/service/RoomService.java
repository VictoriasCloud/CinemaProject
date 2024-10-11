package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Room;
import com.example.cinemaproject.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
    }
    // Поиск комнат по имени
    public List<Room> findRoomsByName(String name) {
        return roomRepository.findByRoomNameContainingIgnoreCase(name);
    }

    // Поиск комнат по номеру зала
    public List<Room> findRoomsByNumber(int number) {
        return roomRepository.findByRoomNumber(number);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    // Создание списка комнат
    public List<Room> createRoomsBatch(List<Room> rooms) {
        return roomRepository.saveAll(rooms);
    }

    // Обновление комнаты
    public Room updateRoom(Long id, Room updatedRoom) {
        Room room = getRoomById(id);
        room.setRoomName(updatedRoom.getRoomName());
        room.setRoomNumber(updatedRoom.getRoomNumber());
        room.setSeatCount(updatedRoom.getSeatCount());
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    // Удаление всех комнат
    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }
}
