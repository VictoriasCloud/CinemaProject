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
        if (sessionRepository.isRoomOccupied(room.getId(), sessionRequestDTO.getStartTime(), endTime)) {
            throw new RuntimeException("Room " + room.getRoomNumber() + "(id=" + room.getId() + ") is occupied by the movie '"
                    + sessionRepository.getMovieByRoomAndTime(room.getId(), sessionRequestDTO.getStartTime(), endTime)
                    + "'(id=" + movie.getId() + ") from " + sessionRequestDTO.getStartTime() + " to " + endTime);
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

        // Создаем места (Seats)
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= room.getSeatCount(); i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(String.valueOf(i));
            seat.setSeatPrice(finalSeatPrice);
            seat.setRoom(room);
            seat.setSession(session);
            seats.add(seat);
        }

        // Сохраняем места
        seatRepository.saveAll(seats);
        session.setSeats(seats);

        // Планируем отправку сообщения о начале сеанса
        sessionSchedulerService.scheduleSessionStart(session);

        // Отправляем сообщение о начале нового сеанса в Kafka (bells)
        String bellsMessage = String.format("Появился новый сеанс на фильм '%s', просмотр будет проводится в зале %d , %s",
                movie.getTitle(), room.getRoomNumber(), session.getStartTime());
        kafkaProducerService.sendBellsMessage(bellsMessage);

        // Отправляем JSON-сообщение о начале сеанса в Kafka (bells)
        kafkaProducerService.sendSessionStartMessage(String.valueOf(room.getRoomNumber()), session.getStartTime());

        // Отправляем рекламное сообщение в Kafka (ads)
        kafkaProducerService.sendAdMessage(String.format("У нас появился новый сеанс на фильм '%s', приходи на просмотр %s",
                movie.getTitle(), session.getStartTime().toString()));

        // Отправляем рекламное JSON-сообщение в Kafka (ads)
        kafkaProducerService.sendAdJsonMessage(movie.getTitle(), session.getStartTime().toString(),
                String.valueOf(room.getRoomNumber()), room.getSeatPrice());

        return session;
    }
    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new RuntimeException("Session not found"));
    }

    // Проверка на наличие пересечений с другими сеансами в указанном зале
    public boolean isSessionTimeAvailable(LocalDateTime startTime, LocalDateTime endTime, int roomNumber) {
        List<Session> existingSessions = sessionRepository.findByRoomRoomNumber(roomNumber);

        for (Session existingSession : existingSessions) {
            if (startTime.isBefore(existingSession.getEndTime()) && endTime.isAfter(existingSession.getStartTime())) {
                return false;  // Есть пересечение
            }
        }
        return true;
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
            if (sessionRepository.isRoomOccupied(session.getRoom().getId(), newStartTime, newEndTime)) {
                throw new RuntimeException("Room " + session.getRoom().getRoomNumber() + " is occupied:( during the new time.");
            }

            // Устанавливаем новое время начала и окончания
            session.setStartTime(newStartTime);
            session.setEndTime(newEndTime);
        }

        // Обновляем цену на места и в самой сессии, если она передана
        if (sessionUpdateDTO.getSeatPrice() != null) {
            // Обновляем цену на места
            List<Seat> seats = seatRepository.findBySessionId(session.getId());
            for (Seat seat : seats) {
                seat.setSeatPrice(sessionUpdateDTO.getSeatPrice());
            }
            seatRepository.saveAll(seats);

            // Обновляем цену в таблице сессии
            session.setSeatPrice(sessionUpdateDTO.getSeatPrice());
        }

        // Сохраняем обновлённую сессию
        Session updatedSession = sessionRepository.save(session);
        System.out.println("Sending ad message: " + "create session");
        kafkaProducerService.sendAdMessage("create session");
        String message;
        message = "message";
        System.out.println("Attempting to send message to theatre.infra.ads: " + message);
        kafkaProducerService.sendAdMessage(message);


        // Отправляем рекламное сообщение в Kafka
        String adMessage = "Ad: New session for movie " + session.getMovie().getTitle() + "  at " + session.getStartTime();
        kafkaProducerService.sendAdMessage(adMessage);
        //смс в кафка
        message = String.format("Session for movie '%s' in room %d has been updated. New start time: %s",
                updatedSession.getMovie().getTitle(), updatedSession.getRoom().getRoomNumber(), updatedSession.getStartTime());
        kafkaProducerService.sendBellsMessage(message);  // Отправляем сообщение в Kafka


        return updatedSession;
    }


    // Удаление одной сессии
    public void deleteSession(Long sessionId) {
        // Находим сессию
        if (!sessionRepository.existsById(sessionId)) {
            throw new RuntimeException("Session with ID " + sessionId + " not found.");
        }

        // Удаляем связанные сессией места
        seatRepository.deleteBySessionId(sessionId);

        // Удаляем сессию
        sessionRepository.deleteById(sessionId);

        kafkaProducerService.sendBellsMessage("Session with ID " + sessionId +"have been deleted");
    }

    // Удаление всех сессий
    public void deleteAllSessions() {
        // Удаляем все места
        seatRepository.deleteAll();
        //а затем все сессии
        sessionRepository.deleteAll();

        kafkaProducerService.sendBellsMessage("All sessions have been deleted.");

    }

    public List<Session> getSessionsByDate(LocalDate date) {
        return sessionRepository.findAllByDate(date);
    }

}
