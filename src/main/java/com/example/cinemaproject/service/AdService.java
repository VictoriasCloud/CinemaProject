package com.example.cinemaproject.service;

import org.springframework.stereotype.Service;

// Использование KafkaProducerService для отправки рекламного сообщения
@Service
public class AdService {

    private final KafkaProducerService kafkaProducerService;

    public AdService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    public void sendAd(String roomNumber, String message) {
        String adMessage = String.format("Ad for room %s: %s", roomNumber, message);
        kafkaProducerService.sendAdMessage(adMessage);
    }
}

