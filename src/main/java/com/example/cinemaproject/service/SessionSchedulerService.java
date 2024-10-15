package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Session;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;

@Service
@EnableScheduling
public class SessionSchedulerService {

    private final KafkaProducerService kafkaProducerService;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public SessionSchedulerService(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    // Запланировать отправку JSON-сообщения в момент начала сеанса
    public void scheduleSessionStart(Session session) {
        long delay = Duration.between(LocalDateTime.now(), session.getStartTime()).toMillis();

        // Планируем отправку сообщения
        ScheduledFuture<?> scheduledTask = scheduler.schedule(() -> {
            Map<String, String> message = new LinkedHashMap<>();
            message.put("movieTitle", session.getMovie().getTitle());
            message.put("seatPrice", String.valueOf(session.getSeatPrice()));
            message.put("roomNumber", String.valueOf(session.getRoom().getRoomNumber()));
            message.put("sessionStartTime", session.getStartTime().toString());
            message.put("sessionEndTime", session.getEndTime().toString());
            message.put("currentTime", LocalDateTime.now().toString());

            kafkaProducerService.sendSessionStartJsonMessage(message);
        }, delay, TimeUnit.MILLISECONDS);

        // Сохраняем ссылку на запланированное задание
        scheduledTasks.put(session.getId(), scheduledTask);
    }

    // Отмена ранее запланированной отправки
    public void cancelScheduledSessionStart(Session session) {
        ScheduledFuture<?> scheduledTask = scheduledTasks.get(session.getId());
        if (scheduledTask != null && !scheduledTask.isDone()) {
            scheduledTask.cancel(true); // Отмена задания
            scheduledTasks.remove(session.getId());
        }
    }
}
