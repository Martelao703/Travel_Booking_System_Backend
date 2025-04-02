package com.david.travel_booking_system.util;

import com.david.travel_booking_system.dto.request.crud.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.specialized.BookingDateChangeRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.specification.BookingSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class BookingServiceHelper {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceHelper(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public double calculateTotalPrice(
            double pricePerNight, LocalDateTime plannedCheckInDateTime, LocalDateTime plannedCheckOutDateTime
    ) {
        long days = ChronoUnit.DAYS.between(plannedCheckInDateTime, plannedCheckOutDateTime);
        return pricePerNight * days;
    }

    public void validateCreateRequestDTO(BookingCreateRequestDTO bookingCreateRequestDTO, Room room, User user) {
        // Check active status of user and room
        if (!user.isActive()) {
            throw new IllegalStateException("User is not active");
        }
        if (!room.isActive()) {
            throw new IllegalStateException("Room is not active");
        }

        // Check if number of guests exceeds max capacity of room type
        if (bookingCreateRequestDTO.getNumberOfGuests() > room.getRoomType().getMaxCapacity()) {
            throw new IllegalArgumentException("Number of guests exceeds max capacity of room type");
        }

        // Check for overlapping bookings
        boolean isOverlapping = bookingRepository.areDatesOverlappingOtherBookings(
                room.getId(),
                bookingCreateRequestDTO.getPlannedCheckOutDateTime(),
                bookingCreateRequestDTO.getPlannedCheckInDateTime(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ONGOING)
        );
        if (isOverlapping) {
            throw new IllegalStateException("Room is already booked for the selected dates");
        }
    }

    public void validateBookingDateChange(Booking booking, BookingDateChangeRequestDTO requestDTO) {
        BookingStatus status = booking.getStatus();

        // Only allow updates for PENDING, CONFIRMED, or ONGOING bookings
        if (!(status == BookingStatus.PENDING || status == BookingStatus.CONFIRMED || status == BookingStatus.ONGOING)) {
            throw new IllegalStateException("Can only change dates for 'PENDING', 'CONFIRMED', or 'ONGOING' bookings");
        }

        // Prevent changing check-in for ONGOING bookings
        if (status == BookingStatus.ONGOING && requestDTO.getPlannedCheckInDateTime().isExplicitlySet()) {
            throw new IllegalStateException("Cannot change check-in date for 'ONGOING' bookings");
        }

        // Get new date values (keep existing if not explicitly set)
        LocalDateTime newCheckIn = requestDTO.getPlannedCheckInDateTime().isExplicitlySet()
                ? requestDTO.getPlannedCheckInDateTime().getValue()
                : booking.getPlannedCheckInDateTime();

        LocalDateTime newCheckOut = requestDTO.getPlannedCheckOutDateTime().isExplicitlySet()
                ? requestDTO.getPlannedCheckOutDateTime().getValue()
                : booking.getPlannedCheckOutDateTime();

        // Check if new date range is valid
        if (newCheckIn.isAfter(newCheckOut)) {
            throw new IllegalArgumentException("Check-out date must be after check-in date");
        }

        // Check for overlapping bookings
        boolean isOverlapping = bookingRepository.areDatesOverlappingOtherBookingIdCheck(
                booking.getRoom().getId(),
                newCheckIn,
                newCheckOut,
                booking.getId(),
                List.of(BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ONGOING)
        );

        if (isOverlapping) {
            throw new IllegalStateException("Room is already booked for the selected dates");
        }
    }

    public void validateCheckInRequestDTO(Booking booking) {
        Room room = booking.getRoom();

        // Check if booking is confirmed
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("Check-in only allowed for 'CONFIRMED' bookings.");
        }

        // Check if room is ready for check-in
        if (room.isOccupied()) {
            throw new IllegalStateException("Room is already occupied");
        }
        if (room.isUnderMaintenance()) {
            throw new IllegalStateException("Room is under maintenance");
        }
        if (!room.isCleaned()) {
            throw new IllegalStateException("Room is not cleaned");
        }
    }
}
