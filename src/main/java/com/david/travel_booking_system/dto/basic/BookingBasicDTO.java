package com.david.travel_booking_system.dto.basic;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class BookingBasicDTO {
    private Integer id;
    private Integer userId;
    private Integer roomId;
    private String status;
    private boolean paid;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private double totalPrice;
}