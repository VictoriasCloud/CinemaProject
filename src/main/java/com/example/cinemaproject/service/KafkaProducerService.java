package com.example.cinemaproject.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendAdMessage(String message) {
        kafkaTemplate.send("theatre.infra.ads", message);
        System.out.println("Sent message to theatre.infra.ads: " + message);
    }
}
