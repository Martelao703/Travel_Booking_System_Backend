package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.BookingRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserService userService, RoomService roomService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.roomService = roomService;
    }

    
    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Booking createBooking(@Valid BookingCreateRequestDTO bookingCreateRequestDTO) {
        // Find associated User and Room
        User user = userService.getUserById(bookingCreateRequestDTO.getUserId());
        Room room = roomService.getRoomById(bookingCreateRequestDTO.getRoomId());

        // Check if property is active
        if (!room.getRoomType().getProperty().isActive()) {
            throw new IllegalArgumentException("Property is not active");
        }

        // Check if room is active
        if (!room.isActive()) {
            throw new IllegalArgumentException("Room is not active");
        }

        // Check if number of guests exceeds max capacity of room type
        if (bookingCreateRequestDTO.getNumberOfGuests() > room.getRoomType().getMaxCapacity()) {
            throw new IllegalArgumentException("Number of guests exceeds max capacity of room type");
        }

        // Check for overlapping bookings
        boolean isOverlapping = bookingRepository.existsByRoom_IdAndCheckInDateBeforeAndCheckOutDateAfter(
                room.getId(),
                bookingCreateRequestDTO.getCheckOutDate(),
                bookingCreateRequestDTO.getCheckInDate()
        );
        if (isOverlapping) {
            throw new IllegalArgumentException("Room is already booked for the selected dates");
        }

        // Build Booking object from DTO
        Booking booking = new Booking(
                user,
                room,
                bookingCreateRequestDTO.getCheckInDate(),
                bookingCreateRequestDTO.getCheckOutDate(),
                bookingCreateRequestDTO.getNumberOfGuests(),
                calculateTotalPrice(room.getRoomType().getPricePerNight(), bookingCreateRequestDTO.getCheckInDate(),
                        bookingCreateRequestDTO.getCheckOutDate())
        );

        // Add the new Booking to its User's and Room's bookings list
        user.getBookings().add(booking);
        room.getBookings().add(booking);

        // Save Booking
        bookingRepository.save(booking);

        return booking;
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Booking getBookingById(Integer bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId + " not found"));
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */

    /* Helper methods ---------------------------------------------------------------------------------------------- */

    public boolean existsBookingsForProperty(Integer propertyId) {
        return bookingRepository.existsBookingsForProperty(propertyId);
    }

    public boolean existsBookingsForRoomType(Integer roomTypeId) {
        return bookingRepository.existsBookingsForRoomType(roomTypeId);
    }

    public boolean existsBookingsForRoom(Integer roomId) {
        return bookingRepository.existsBookingsForRoom(roomId);
    }

    public boolean existsBookingsForBed(Integer bedId) {
        return bookingRepository.existsBookingsForBed(bedId);
    }

    // Calculate total price of booking
    private double calculateTotalPrice(double pricePerNight, LocalDate checkInDate, LocalDate checkOutDate) {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return pricePerNight * days;
    }
}
