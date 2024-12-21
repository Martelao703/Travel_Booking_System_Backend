package com.david.travel_booking_system.dto.basic;

import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.validation.ValidDateRange;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@ValidDateRange
public class BookingBasicDTO {
    private Integer id;
    private Integer userId;
    private Integer roomId;
    private String status;
    private boolean isPaid;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private double totalPrice;

    public BookingBasicDTO(Integer id, Integer userId, Integer roomId, String status, boolean isPaid, LocalDate checkInDate,
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

    public static BookingBasicDTO from(Booking booking) {
        return new BookingBasicDTO(
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

    public static List<BookingBasicDTO> from(List<Booking> bookings) {
        return bookings.stream().map(BookingBasicDTO::from).collect(Collectors.toList());
    }
}