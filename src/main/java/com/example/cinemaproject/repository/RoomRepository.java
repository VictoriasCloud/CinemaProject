package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByRoomNameContainingIgnoreCase(String roomName, Pageable pageable);
    Page<Room> findByRoomNumber(int roomNumber, Pageable pageable);
}
