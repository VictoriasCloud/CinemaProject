package com.example.cinemaproject.controller;

import com.example.cinemaproject.service.KafkaProducerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
public class AdController {

    private final KafkaProducerService kafkaProducerService;

    public AdController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping("/send")
    public String sendAdMessage(@RequestParam String message) {
        kafkaProducerService.sendAdMessage(message);
        return "Message sent to theatre.infra.ads";
    }
}
