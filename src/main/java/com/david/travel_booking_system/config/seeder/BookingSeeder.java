package com.david.travel_booking_system.config.seeder;

import com.david.travel_booking_system.config.DataSeeder;
import com.david.travel_booking_system.dto.request.crud.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.service.BookingService;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("dev")
@Order(6)
public class BookingSeeder implements DataSeeder {

    private final BookingService bookingService;

    public BookingSeeder(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void seed() {
        /* ------------------------ Bookings for Admin User ------------------------ */

        /* Bookings for Admin Property */

        // Id - 1 --> Booking in Single Room
        seedBooking(1, 1,
                LocalDateTime.of(2026, 10, 1, 14, 0),
                LocalDateTime.of(2026, 10, 5, 11, 0),
                1);

        // Id - 2 --> Booking in Deluxe Suite
        seedBooking(1, 4,
                LocalDateTime.of(2026, 10, 10, 14, 0),
                LocalDateTime.of(2026, 10, 15, 11, 0),
                2);

        /* Bookings for Host Property */

        // Id - 3 --> Booking in 2-Bedroom Apartment
        seedBooking(1, 7,
                LocalDateTime.of(2026, 10, 20, 14, 0),
                LocalDateTime.of(2026, 10, 25, 11, 0),
                3);

        /* ------------------------ Bookings for John Doe ------------------------ */

        /* Bookings for Admin Property */

        // Id - 4 --> Booking in Single Room
        seedBooking(4, 2,
                LocalDateTime.of(2026, 11, 1, 14, 0),
                LocalDateTime.of(2026, 11, 3, 11, 0),
                1);

        // Id - 5 --> Booking in Deluxe Suite
        seedBooking(4, 5,
                LocalDateTime.of(2026, 11, 5, 14, 0),
                LocalDateTime.of(2026, 11, 10, 11, 0),
                2);

        /* Bookings for Host Property */

        // Id - 6 --> Booking in 2-Bedroom Apartment
        seedBooking(4, 8,
                LocalDateTime.of(2026, 11, 15, 14, 0),
                LocalDateTime.of(2026, 11, 20, 11, 0),
                4);
    }

    private void seedBooking(Integer userId,
                             Integer roomId,
                             LocalDateTime plannedCheckInDateTime,
                             LocalDateTime plannedCheckOutDateTime,
                             int numberOfGuests) {

        // Build BookingCreateRequestDTO
        BookingCreateRequestDTO bookingCreateRequestDTO = BookingCreateRequestDTO.builder()
                .userId(userId)
                .roomId(roomId)
                .plannedCheckInDateTime(plannedCheckInDateTime)
                .plannedCheckOutDateTime(plannedCheckOutDateTime)
                .numberOfGuests(numberOfGuests)
                .build();

        // Save the booking
        bookingService.createBooking(bookingCreateRequestDTO);
    }
}
