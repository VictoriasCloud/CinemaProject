package com.example.cinemaproject.service;

import com.example.cinemaproject.repository.SessionRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListenerService {

    private final SessionRepository sessionRepository;

    public KafkaListenerService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @KafkaListener(topics = "theatre.infra.bells", groupId = "cinema-group")
    public void listenBells(String message) {
        System.out.println("Received message in group 'cinema-group': " + message);
    }

    @KafkaListener(topics = "theatre.infra.newads", groupId = "cinema-group")
    public void listenAds(String message) {
        System.out.println("Received ad message in group 'cinema-group': " + message);
    }

}
