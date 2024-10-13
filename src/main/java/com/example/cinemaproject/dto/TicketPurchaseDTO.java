package com.example.cinemaproject.dto;

public class TicketPurchaseDTO {
    //используется для получения данных от клиента
    // при создании нового билета. Он содержит информацию,
    // которая необходима для создания билета
    private Long seatId;
    private String email;
    private String fullName;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}

