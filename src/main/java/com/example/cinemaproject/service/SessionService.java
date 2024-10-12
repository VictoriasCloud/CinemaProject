package com.example.cinemaproject.service;

import com.example.cinemaproject.model.Movie;
import com.example.cinemaproject.model.Room;
import com.example.cinemaproject.model.Seat;
import com.example.cinemaproject.model.Session;
import com.example.cinemaproject.repository.MovieRepository;
import com.example.cinemaproject.repository.RoomRepository;
import com.example.cinemaproject.repository.SeatRepository;
import com.example.cinemaproject.repository.SessionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final SeatRepository seatRepository;

    public SessionService(SessionRepository sessionRepository, RoomRepository roomRepository,
                          MovieRepository movieRepository, SeatRepository seatRepository) {
        this.sessionRepository = sessionRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.seatRepository = seatRepository;
    }

    public Session createSession(Long movieId, Long roomId, LocalDateTime startTime) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Вычисляем конец сеанса на основе длительности фильма
        LocalDateTime endTime = startTime.plusMinutes(movie.getDuration());

        // Создаём сеанс
        Session session = new Session();
        session.setMovie(movie);
        session.setRoom(room);
        session.setStartTime(startTime);
        session.setEndTime(endTime);

        session = sessionRepository.save(session);

        // Создаём места (Seats) на основе количества мест в зале
        List<Seat> seats = new ArrayList<>();
        for (int i = 1; i <= room.getSeatCount(); i++) {
            Seat seat = new Seat();
            seat.setSeatNumber(String.valueOf(i));
            seat.setSeatPrice(room.getSeatPrice());
            seat.setRoom(room);
            seat.setSession(session);
            seats.add(seat);
        }

        // Сохраняем места
        seatRepository.saveAll(seats);
        session.setSeats(seats);

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

        // Проверка пересечения времени
        for (Session existingSession : existingSessions) {
            if (startTime.isBefore(existingSession.getEndTime()) && endTime.isAfter(existingSession.getStartTime())) {
                return false;  // Есть пересечение
            }
        }
        return true;  // Время свободно
    }

    public Session updateSession(Long id, Session sessionDetails) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setMovie(sessionDetails.getMovie());
        session.setRoom(sessionDetails.getRoom());
        session.setStartTime(sessionDetails.getStartTime());
        session.setEndTime(sessionDetails.getEndTime());

        return sessionRepository.save(session);
    }

//    public List<Session> findSessionsByMovieTitle(String title) {
//        return sessionRepository.findByMovieTitle(title);
//    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}
