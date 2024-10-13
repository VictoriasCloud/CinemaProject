package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Поиск пользователя по email
    Optional<User> findByEmail(String email);
}
