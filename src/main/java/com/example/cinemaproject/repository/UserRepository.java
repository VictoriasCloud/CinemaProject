package com.example.cinemaproject.repository;

import com.example.cinemaproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Найти пользователя по email
    User findByEmail(String email);
}
