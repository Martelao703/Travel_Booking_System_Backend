package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.validation.ValidDateRange;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ValidDateRange
public class BookingDTO {
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @NotNull(message = "User ID cannot be null")
    private Integer userId;

    @NotNull(message = "Room ID cannot be null")
    private Integer roomId;

    @NotNull(message = "Booking status cannot be null")
    private String status;

    @NotNull(message = "Paid status cannot be null")
    private boolean isPaid;

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

    public BookingDTO(Integer id, Integer userId, Integer roomId, String status, boolean isPaid, LocalDate checkInDate,
                      LocalDate checkOutDate, int numberOfGuests, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.status = status;
        this.isPaid = isPaid;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalPrice = totalPrice;
    }

    public static BookingDTO from(Booking booking) {
        return new BookingDTO(
            booking.getId(),
            booking.getUser().getId(),
            booking.getRoom().getId(),
            booking.getStatus().toString(),
            booking.isPaid(),
            booking.getCheckInDate(),
            booking.getCheckOutDate(),
            booking.getNumberOfGuests(),
            booking.getTotalPrice()
        );
    }

    public static List<BookingDTO> from(List<Booking> bookings) {
        return bookings.stream().map(BookingDTO::from).collect(Collectors.toList());
    }
}