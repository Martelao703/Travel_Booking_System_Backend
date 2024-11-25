package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        /*User user = userService.getUserById(bookingCreateRequestDTO.getUserId());
        Room room = roomService.getRoomById(bookingCreateRequestDTO.getRoomId());*/
        return null;
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
}