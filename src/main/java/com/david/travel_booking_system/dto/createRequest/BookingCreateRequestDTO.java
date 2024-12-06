package com.david.travel_booking_system.dto.createRequest;

import com.david.travel_booking_system.validation.ValidDateRange;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@ValidDateRange
public class BookingCreateRequestDTO {
    @NotNull(message = "User ID cannot be null")
    private Integer userId;

    @NotNull(message = "Room ID cannot be null")
    private Integer roomId;

    @NotNull(message = "Check-in date cannot be null")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;
}
