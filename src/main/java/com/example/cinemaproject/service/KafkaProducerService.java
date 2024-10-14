package com.example.cinemaproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendAdMessage(String message) {
        kafkaTemplate.send("theatre.infra.newads", message);
    }


    public void sendBellsMessage(String message) {
        kafkaTemplate.send("theatre.infra.bells", message);
        System.out.println("Sent message to theatre.infra.bells: " + message);
    }

    public void sendSessionStartMessage(String roomNumber, LocalDateTime startTime) {
        try {
            // Создание JSON-объекта
            Map<String, String> message = new HashMap<>();
            message.put("roomNumber", roomNumber);
            message.put("startTime", startTime.toString());

            // Преобразование объекта в строку JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(message);

            // Отправка сообщения в Kafka
            kafkaTemplate.send("theatre.infra.bells", jsonMessage);
            System.out.println("Отправлено сообщение в theatre.infra.bells: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод для отправки JSON-сообщений
    public void sendAdJsonMessage(String movieTitle, String sessionStartTime, String roomNumber, double seatPrice) {
        try {
            // Создание JSON-объекта
            Map<String, Object> adDetails = new HashMap<>();
            adDetails.put("movieTitle", movieTitle);
            adDetails.put("sessionStartTime", sessionStartTime);
            adDetails.put("roomNumber", roomNumber);
            adDetails.put("seatPrice", seatPrice);

            // Преобразование объекта в строку JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonAdMessage = objectMapper.writeValueAsString(adDetails);

            // Отправка сообщения в Kafka
            kafkaTemplate.send("theatre.infra.ads", jsonAdMessage);
            System.out.println("Отправлено JSON-сообщение: " + jsonAdMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}