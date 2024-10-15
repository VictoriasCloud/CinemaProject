package com.example.cinemaproject.service;

import com.example.cinemaproject.dto.SessionRequestDTO;
import com.example.cinemaproject.dto.SessionUpdateDTO;
import com.example.cinemaproject.model.Movie;
import com.example.cinemaproject.model.Room;
import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.repository.MovieRepository;
import com.example.cinemaproject.repository.RoomRepository;
import com.example.cinemaproject.repository.SeatRepository;
import com.example.cinemaproject.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;

    @Autowired
    private SessionSchedulerService sessionSchedulerService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public SessionService(SessionRepository sessionRepository, RoomRepository roomRepository,
                          MovieRepository movieRepository, SeatRepository seatRepository,
                          KafkaProducerService kafkaProducerService) {
        this.sessionRepository = sessionRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.seatRepository = seatRepository;
        this.kafkaProducerService = kafkaProducerService;
    }


    public Session createSession(SessionRequestDTO sessionRequestDTO) {
        Movie movie = movieRepository.findById(sessionRequestDTO.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Room room = roomRepository.findById(sessionRequestDTO.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Вычисляем конец сеанса на основе длительности фильма
        LocalDateTime endTime = sessionRequestDTO.getStartTime().plusMinutes(movie.getDuration());

        // Проверка на занятость зала
        Session occupiedSession = sessionRepository.getSessionByRoomAndTime(room.getId(), sessionRequestDTO.getStartTime(), endTime);
        if (occupiedSession != null) {
            throw new RuntimeException("Room " + room.getRoomNumber() + "(id=" + room.getId() + ") is occupied by the movie '"
                    + occupiedSession.getMovie().getTitle() + "'(id=" + occupiedSession.getMovie().getId() + ") from "
                    + occupiedSession.getStartTime() + " to " + occupiedSession.getEndTime());
        }

        // Вычисляем цену на сеанс
        double finalSeatPrice = room.getSeatPrice();
        if (sessionRequestDTO.getTicketPriceModifier() != null) {
            finalSeatPrice += sessionRequestDTO.getTicketPriceModifier();
        }

        // Создаем сеанс
        Session session = new Session();
        session.setMovie(movie);
        session.setRoom(room);
        session.setStartTime(sessionRequestDTO.getStartTime());
        session.setEndTime(endTime);
        session.setSeatPrice(finalSeatPrice);

        session = sessionRepository.save(session);

        // Создаем места
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= room.getSeatCount(); i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(String.valueOf(i));
            seat.setSeatPrice(finalSeatPrice);
            seat.setRoom(room);
            seat.setSession(session);
            seats.add(seat);
        }
        seatRepository.saveAll(seats);
        session.setSeats(seats);

        // Планируем отправку сообщения о начале сеанса в bells
        sessionSchedulerService.scheduleSessionStart(session);

        // Отправляем актуальное рекламное JSON-сообщение в ads
        kafkaProducerService.sendAdJsonMessage(movie.getTitle(), finalSeatPrice,
                String.valueOf(room.getRoomNumber()), session.getStartTime().toString(), endTime.toString());

        return session;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }


    public Session updateSession(Long sessionId, SessionUpdateDTO sessionUpdateDTO) {
        // Находим сессию по ID
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Если startTime передан и отличается от текущего времени сессии
        if (sessionUpdateDTO.getStartTime() != null && !session.getStartTime().equals(sessionUpdateDTO.getStartTime())) {
            LocalDateTime newStartTime = sessionUpdateDTO.getStartTime();
            LocalDateTime newEndTime = newStartTime.plusMinutes(session.getMovie().getDuration());

            // Проверка на занятость зала на новое время
            Session occupiedSession = sessionRepository.getSessionByRoomAndTime(session.getRoom().getId(), newStartTime, newEndTime);
            if (occupiedSession != null && !occupiedSession.getId().equals(session.getId())) {
                throw new RuntimeException("Room " + session.getRoom().getRoomNumber() + " is occupied by the movie '"
                        + occupiedSession.getMovie().getTitle() + "'(id=" + occupiedSession.getMovie().getId() + ") from "
                        + occupiedSession.getStartTime() + " to " + occupiedSession.getEndTime());
            }

            // Устанавливаем новое время начала и окончания
            session.setStartTime(newStartTime);
            session.setEndTime(newEndTime);

            // Отменяем старое расписание и запланируем новое
            sessionSchedulerService.cancelScheduledSessionStart(session); // Отменяем старое задание
            sessionSchedulerService.scheduleSessionStart(session); // Планируем новое задание
        }

        // Обновляем цену на места и в самой сессии, если передана новая цена
        if (sessionUpdateDTO.getSeatPrice() != null) {
            List<Seat> seats = seatRepository.findBySessionId(session.getId());
            for (Seat seat : seats) {
                seat.setSeatPrice(sessionUpdateDTO.getSeatPrice());
            }
            session.setSeatPrice(sessionUpdateDTO.getSeatPrice());
            seatRepository.saveAll(seats);
        }

        // Сохраняем обновлённую сессию
        Session updatedSession = sessionRepository.save(session);

        return updatedSession;
    }

    // Удаление одной сессии
    public void deleteSession(Long sessionId) {
        // Находим сессию
        if (!sessionRepository.existsById(sessionId)) {
            throw new RuntimeException("Session with ID " + sessionId + " not found.");
        }

        // Отменяем запланированную задачу в расписании, если она существует
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        sessionSchedulerService.cancelScheduledSessionStart(session);

        // Удаляем связанные сессией места
        seatRepository.deleteBySessionId(sessionId);

        // Удаляем сессию
        sessionRepository.deleteById(sessionId);

        System.out.println("Session with ID " + sessionId + " has been deleted");
    }

    // Удаление всех сессий
    public void deleteAllSessions() {
        // Находим все сессии
        List<Session> sessions = sessionRepository.findAll();

        // Отменяем запланированные задачи для каждой сессии в расписании
        for (Session session : sessions) {
            sessionSchedulerService.cancelScheduledSessionStart(session);
        }
        // Удаляем все места
        seatRepository.deleteAll();
        //а затем все сессии
        sessionRepository.deleteAll();
        System.out.println("All sessions have been deleted");

    }

    public List<Session> getSessionsByDate(LocalDate date) {
        return sessionRepository.findAllByDate(date);
    }

}
