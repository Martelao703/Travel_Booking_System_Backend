package com.david.travel_booking_system.dto.request.updateRequest;

import com.david.travel_booking_system.enums.BookingStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingUpdateRequestDTO {
    @NotNull(message = "Booking status cannot be null")
    private BookingStatus status = BookingStatus.PENDING;

    @NotNull(message = "Paid status cannot be null")
    private boolean isPaid = false;

    @NotNull(message = "Check-in date cannot be null")
    @Future(message = "Check-in date must be in the future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date cannot be null")
    @Future(message = "Check-out date must be in the future")
    private LocalDate checkOutDate;

    @NotNull(message = "Number of guests cannot be null")
    @Min(value = 1, message = "Number of guests must be at least 1")
    private int numberOfGuests;

    @NotNull(message = "Total price cannot be null")
    @Min(value = 0, message = "Total price must be greater than 0")
    private double totalPrice;
}
