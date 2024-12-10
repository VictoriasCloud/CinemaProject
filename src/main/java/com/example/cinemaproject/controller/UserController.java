package com.example.cinemaproject.controller;

import com.example.cinemaproject.model.User;
import com.example.cinemaproject.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Создание пользователя
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // Получение списка всех пользователей
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Получение пользователя по ID
    @GetMapping(params = "id")
    public ResponseEntity<User> getUserById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Редактирование пользователя
    @PutMapping
    public ResponseEntity<User> updateUser(@RequestParam Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user.getEmail(), user.getFullName());
        return ResponseEntity.ok(updatedUser);
    }

    // Удаление пользователя по ID
    @DeleteMapping(params = "id")
    public ResponseEntity<Void> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Удаление всех пользователей
    @DeleteMapping
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.noContent().build();
    }
}
