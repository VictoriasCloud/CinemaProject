package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Room;
import com.example.cinemaproject.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<Room> getAllRooms(Pageable pageable) {
        return roomRepository.findAll(pageable);
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Page<Room> findRoomsByName(String name, Pageable pageable) {
        return roomRepository.findByRoomNameContainingIgnoreCase(name, pageable);
    }

    public Page<Room> findRoomsByNumber(int number, Pageable pageable) {
        return roomRepository.findByRoomNumber(number, pageable);
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public List<Room> createRoomsBatch(List<Room> rooms) {
        return roomRepository.saveAll(rooms);
    }

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

    public void deleteAllRooms() {
        roomRepository.deleteAll();
    }
}
