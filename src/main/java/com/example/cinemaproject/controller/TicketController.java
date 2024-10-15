package com.example.cinemaproject.controller;

import com.example.cinemaproject.dto.TicketPurchaseDTO;
import com.example.cinemaproject.dto.TicketResponseDTO;
import com.example.cinemaproject.model.Ticket;
import com.example.cinemaproject.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Создание
    @PostMapping
    public ResponseEntity<Ticket> createTicket(@RequestBody TicketPurchaseDTO ticketPurchaseDTO) {
        Ticket ticket = ticketService.createTicket(ticketPurchaseDTO);
        return ResponseEntity.ok(ticket);
    }

    // Получить билеты по ID пользователя
    @GetMapping("/user/")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByUserId(@RequestParam Long userId) {
        List<TicketResponseDTO> tickets = ticketService.getTicketsByUserId(userId);

        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если билеты не найдены
        }

        return ResponseEntity.ok(tickets);
    }

    // Получение всех билетов с DTO
    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets() {
        List<TicketResponseDTO> tickets = ticketService.getAllTickets();

        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если билетов нет
        }

        return ResponseEntity.ok(tickets);
    }

    // Get билеты по имени пользователя
    @GetMapping("/user/search")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByUserFullName(@RequestParam String fullName) {
        List<TicketResponseDTO> tickets = ticketService.getTicketsByUserFullName(fullName);

        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если билеты не найдены по имени пользователя
        }

        return ResponseEntity.ok(tickets);
    }


    // Get билеты по статусу (купленные или возвращенные)
    @GetMapping("/status/")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsByStatus(@RequestParam Ticket.TicketStatus status) {
        List<TicketResponseDTO> tickets = ticketService.getTicketsByStatus(status);

        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если билеты с таким статусом не найдены
        }

        return ResponseEntity.ok(tickets);
    }

    // Get билеты по ID сеанса
    @GetMapping("/session/")
    public ResponseEntity<List<TicketResponseDTO>> getTicketsBySessionId(@RequestParam Long sessionId) {
        List<TicketResponseDTO> tickets = ticketService.getTicketsBySessionId(sessionId);

        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если билеты на этот сеанс не найдены
        }

        return ResponseEntity.ok(tickets);
    }



    // Возврат
    @PutMapping("/")
    public ResponseEntity<TicketResponseDTO> returnTicket(@RequestParam Long id) {
        try {
            TicketResponseDTO ticket = ticketService.returnTicket(id);
            return ResponseEntity.ok(ticket);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // Если билет не найден
        }
    }


    // Удаление билета по ID
    @DeleteMapping("/")
    public ResponseEntity<?> deleteTicket(@RequestParam Long id) {
        try {
            ticketService.deleteTicket(id);
            return ResponseEntity.ok("Ticket has been deleted.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found."); // Если билет не найден
        }
    }

    // Удаление всех билетов
    @DeleteMapping
    public ResponseEntity<?> deleteAllTickets() {
        ticketService.deleteAllTickets();
        return ResponseEntity.ok("All tickets have been deleted.");
    }
}
