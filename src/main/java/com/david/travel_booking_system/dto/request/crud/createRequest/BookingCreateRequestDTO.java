package com.david.travel_booking_system.dto.request.crud.createRequest;

import com.david.travel_booking_system.validation.annotation.ValidDateRange;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ValidDateRange
public class BookingCreateRequestDTO {
    @NotNull(message = "User ID cannot be null")
    private Integer userId;

    @NotNull(message = "Room ID cannot be null")
    private Integer roomId;

    @NotNull(message = "Planned Check-in date-time cannot be null")
    @Future(message = "Planned Check-in date-time must be in the future")
    private LocalDateTime plannedCheckInDateTime;

    @NotNull(message = "Planned Check-out date-time cannot be null")
    @Future(message = "Planned Check-out date-time must be in the future")
    private LocalDateTime plannedCheckOutDateTime;

    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;
}
