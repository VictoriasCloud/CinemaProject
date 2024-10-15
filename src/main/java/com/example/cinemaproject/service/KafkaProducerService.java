package com.example.cinemaproject.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    // Метод для отправки JSON-сообщений в рекламу
    public void sendAdJsonMessage(String movieTitle, double seatPrice, String roomNumber, String sessionStartTime, String sessionEndTime) {
        try {
            // Используем LinkedHashMap для гарантии порядка полей
            Map<String, Object> message = new LinkedHashMap<>();
            message.put("movieTitle", movieTitle);
            message.put("seatPrice", seatPrice);
            message.put("roomNumber", roomNumber);
            message.put("sessionStartTime", sessionStartTime);
            message.put("sessionEndTime", sessionEndTime);

            // Преобразование объекта в строку JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(message);

            // Отправка сообщения в Kafka
            kafkaTemplate.send("theatre.infra.ads", jsonMessage);
            System.out.println("Отправлено JSON-сообщение: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Отправка JSON-сообщения о начале сеанса
    public void sendSessionStartJsonMessage(Map<String, String> message) {
        try {
            // Преобразование объекта в строку JSON
            String jsonMessage = objectMapper.writeValueAsString(message);

            // Отправка JSON в топик theatre.infra.bells
            kafkaTemplate.send("theatre.infra.bells", jsonMessage);
            System.out.println("Отправлено JSON-сообщение в theatre.infra.bells: " + jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void sendBellsMessage(String message) {
//        kafkaTemplate.send("theatre.infra.bells", message);
//        System.out.println("Sent message to theatre.infra.bells: " + message);
//    }
//
//    public void sendSessionStartMessage(String roomNumber, LocalDateTime startTime) {
//        try {
//            // Создание JSON-объекта
//            Map<String, String> message = new HashMap<>();
//            message.put("roomNumber", roomNumber);
//            message.put("startTime", startTime.toString());
//
//            // Преобразование объекта в строку JSON
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonMessage = objectMapper.writeValueAsString(message);
//
//            // Отправка сообщения в Kafka
//            kafkaTemplate.send("theatre.infra.bells", jsonMessage);
//            System.out.println("Отправлено сообщение в theatre.infra.bells: " + jsonMessage);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}