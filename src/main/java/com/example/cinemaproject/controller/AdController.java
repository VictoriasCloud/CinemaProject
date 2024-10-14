package com.example.cinemaproject.controller;

import com.example.cinemaproject.service.AdService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ads")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendAd(@RequestParam String roomNumber, @RequestParam String message) {
        adService.sendAd(roomNumber, message);
        return ResponseEntity.ok("Ad message sent successfully");
    }
}

