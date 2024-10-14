package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Session;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//@Scheduled для периодических задач
@Service
@EnableScheduling
public class SessionSchedulerService {

    private final KafkaProducerService kafkaProducerService;
    private final ScheduledExecutorService scheduler;

    public SessionSchedulerService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
        this.scheduler = Executors.newScheduledThreadPool(1); // Пул потоков для планирования задач
    }

    // Метод для планирования отправки сообщения о начале сеанса
    public void scheduleSessionStart(Session session) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sessionStartTime = session.getStartTime();

        // Рассчитываем задержку до начала сеанса
        long delay = Duration.between(now, sessionStartTime).toMillis();

        if (delay > 0) {
            // Планируем задачу на будущее время начала сеанса
            scheduler.schedule(() -> sendSessionStartMessage(session), delay, TimeUnit.MILLISECONDS);
        } else {
            // Если время сеанса уже прошло (например, тестирование), сообщение отправляется сразу
            sendSessionStartMessage(session);
        }
    }

    // Метод для отправки сообщения о начале сеанса
    private void sendSessionStartMessage(Session session) {
        String roomNumber = String.valueOf(session.getRoom().getRoomNumber());
        LocalDateTime startTime = session.getStartTime();

        // Отправляем сообщение в Kafka (JSON)
        kafkaProducerService.sendSessionStartMessage(roomNumber, startTime);
    }
}
