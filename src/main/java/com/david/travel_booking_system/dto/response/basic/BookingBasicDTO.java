package com.david.travel_booking_system.dto.response.basic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingBasicDTO {
    private Integer id;
    private Integer userId;
    private Integer roomId;
    private String status;
    private boolean paid;
    private LocalDateTime plannedCheckInDateTime;
    private LocalDateTime plannedCheckOutDateTime;
    private int numberOfGuests;
    private double totalPrice;
}