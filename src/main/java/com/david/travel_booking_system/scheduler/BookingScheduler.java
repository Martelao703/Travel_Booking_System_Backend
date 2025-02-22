package com.david.travel_booking_system.scheduler;

import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingScheduler {
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    // Runs every 15 minutes to check for expired bookings
    @Scheduled(fixedRate = 900000)
    public void checkForExpiredBookings() {
        LocalDateTime expiryThreshold = LocalDateTime.now().minusHours(24);

        List<Booking> expiredBookings = bookingRepository.findExpiredBookings(
                BookingStatus.PENDING, expiryThreshold
        );

        expiredBookings.forEach(bookingService::markBookingAsExpired);
    }

    // Runs every hour to check for no-show bookings
    @Scheduled(fixedRate = 3600000)
    public void checkForNoShowBookings() {
        LocalDateTime noShowThreshold = LocalDateTime.now().minusHours(6);

        List<Booking> noShowBookings = bookingRepository.findConfirmedBookingsPastCheckInTimeWithoutCheckIn(
                BookingStatus.CONFIRMED, noShowThreshold
        );

        noShowBookings.forEach(bookingService::markBookingAsNoShow);
    }
}
