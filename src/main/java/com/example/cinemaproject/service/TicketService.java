package com.example.cinemaproject.service;

import com.example.cinemaproject.dto.TicketPurchaseDTO;
import com.example.cinemaproject.dto.TicketResponseDTO;
import com.example.cinemaproject.model.*;
import com.example.cinemaproject.repository.SeatRepository;
import com.example.cinemaproject.repository.SessionRepository;
import com.example.cinemaproject.repository.TicketRepository;
import com.example.cinemaproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    public TicketService(TicketRepository ticketRepository, SeatRepository seatRepository,
                         UserRepository userRepository, SessionRepository sessionRepository) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    // Преобразование сущности Ticket в TicketResponseDTO
    private TicketResponseDTO mapToTicketResponseDTO(Ticket ticket) {
        TicketResponseDTO dto = new TicketResponseDTO();
        dto.setId(ticket.getId());
        dto.setSeatNumber(ticket.getSeat().getSeatNumber());
        dto.setSeatPrice(ticket.getSeat().getSeatPrice());
        dto.setSeatStatus(ticket.getSeat().getSeatStatus().name());
        dto.setMovieTitle(ticket.getSession().getMovie().getTitle());
        dto.setSessionStartTime(ticket.getSession().getStartTime());
        dto.setSessionEndTime(ticket.getSession().getEndTime());
        dto.setUserEmail(ticket.getUser().getEmail());
        dto.setUserFullName(ticket.getUser().getFullName());
        dto.setTicketStatus(ticket.getTicketStatus().name());
        return dto;
    }

    public Ticket createTicket(TicketPurchaseDTO ticketPurchaseDTO) {
        // Найти место
        Seat seat = seatRepository.findById(ticketPurchaseDTO.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found"));

        // Проверить, свободно ли место
        if (seat.getSeatStatus() == Seat.SeatStatus.OCCUPIED) {
            throw new RuntimeException("Seat is already occupied.");
        }

        // Найти или создать пользователя
        User user = userRepository.findByEmail(ticketPurchaseDTO.getEmail())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(ticketPurchaseDTO.getEmail());
                    newUser.setFullName(ticketPurchaseDTO.getFullName());
                    return userRepository.save(newUser);
                });

        // Получить сессию, связанную с местом
        Session session = seat.getSession();

        // Создать билет
        Ticket ticket = new Ticket();
        ticket.setSeat(seat);
        ticket.setSession(session);
        ticket.setUser(user);
        ticket.setSeatPrice(seat.getSeatPrice());
        ticket.setSeatNumber(seat.getSeatNumber());

        // Изменить статус места на "занято"
        seat.setSeatStatus(Seat.SeatStatus.OCCUPIED);
        seatRepository.save(seat);

        return ticketRepository.save(ticket);
    }

    // Получение всех билетов и преобразование в DTO
    public List<TicketResponseDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found.");
        }
        return tickets.stream().map(this::mapToTicketResponseDTO).collect(Collectors.toList());
    }

    // Получение билетов по ID пользователя
    public List<TicketResponseDTO> getTicketsByUserId(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found for user with ID " + userId);
        }
        return tickets.stream().map(this::mapToTicketResponseDTO).collect(Collectors.toList());
    }

    // Получение билетов по имени пользователя
    public List<TicketResponseDTO> getTicketsByUserFullName(String fullName) {
        List<Ticket> tickets = ticketRepository.findByUserFullName(fullName);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found for user with full name " + fullName);
        }
        return tickets.stream().map(this::mapToTicketResponseDTO).collect(Collectors.toList());
    }

    // Получение билетов по статусу
    public List<TicketResponseDTO> getTicketsByStatus(Ticket.TicketStatus status) {
        List<Ticket> tickets = ticketRepository.findByTicketStatus(status);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found with status " + status);
        }
        return tickets.stream().map(this::mapToTicketResponseDTO).collect(Collectors.toList());
    }

    // Получение билетов по ID сеанса
    public List<TicketResponseDTO> getTicketsBySessionId(Long sessionId) {
        List<Ticket> tickets = ticketRepository.findBySessionId(sessionId);
        if (tickets.isEmpty()) {
            throw new RuntimeException("No tickets found for session with ID " + sessionId);
        }
        return tickets.stream().map(this::mapToTicketResponseDTO).collect(Collectors.toList());
    }

    // Возврат билета
    public TicketResponseDTO returnTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID " + ticketId));

        // Логика возврата билета
        ticket.setTicketStatus(Ticket.TicketStatus.RETURNED);

        Seat seat = ticket.getSeat();
        seat.setSeatStatus(Seat.SeatStatus.FREE);
        seatRepository.save(seat);

        ticketRepository.save(ticket);

        return mapToTicketResponseDTO(ticket);
    }


    // Удаление билета по ID
    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID " + ticketId));
        ticketRepository.delete(ticket);
    }

    // Удаление всех билетов
    public void deleteAllTickets() {
        if (ticketRepository.count() == 0) {
            throw new RuntimeException("No tickets to delete.");
        }
        ticketRepository.deleteAll();
    }
}
