package com.example.cinemaproject.repository;

import java.util.List;
import com.example.cinemaproject.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // Поиск комнат по имени (с поддержкой частичного совпадения)
    List<Room> findByRoomNameContainingIgnoreCase(String roomName);

    // Поиск комнат по номеру зала
    List<Room> findByRoomNumber(int roomNumber);
}
