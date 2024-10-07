package com.example.cinemaproject.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    @KafkaListener(topics = "theatre.infra.bells", groupId = "cinema-group")
    public void listenBells(String message) {
        System.out.println("Received message in group 'cinema-group': " + message);
        // Обработка сообщения о начале сеанса
        // Например, можно обновлять статус сеанса, когда он начинается
    }
}
