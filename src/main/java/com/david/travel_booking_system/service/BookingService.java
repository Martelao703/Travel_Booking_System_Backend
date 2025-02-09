package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.patchRequest.BookingPatchRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.BookingUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.mapper.BookingMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
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
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserService userService, RoomService roomService,
                          BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.bookingMapper = bookingMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Booking createBooking(BookingCreateRequestDTO bookingCreateRequestDTO) {
        // Find associated User and Room
        User user = userService.getUserById(bookingCreateRequestDTO.getUserId());
        Room room = roomService.getRoomById(bookingCreateRequestDTO.getRoomId());

        validateCreateRequestDTO(bookingCreateRequestDTO, room);

        // Create Booking from DTO
        Booking booking = bookingMapper.createBookingFromDTO(bookingCreateRequestDTO);

        // Calculate total price and set it on the Booking entity
        booking.setTotalPrice(
                calculateTotalPrice(room.getRoomType().getPricePerNight(), bookingCreateRequestDTO.getCheckInDate(),
                        bookingCreateRequestDTO.getCheckOutDate())
        );

        // Add the new Booking to its User's and Room's bookings list
        user.getBookings().add(booking);
        room.getBookings().add(booking);

        return bookingRepository.save(booking);
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

    @Transactional
    public Booking updateBooking(Integer id, BookingUpdateRequestDTO bookingUpdateRequestDTO) {
        Booking booking = getBookingById(id);

        // Validate number of guests
        if (bookingUpdateRequestDTO.getNumberOfGuests() > booking.getRoom().getRoomType().getMaxCapacity()) {
            throw new IllegalArgumentException("Number of guests exceeds max capacity of room type");
        }

        // Update Booking from DTO
        bookingMapper.updateBookingFromDTO(booking, bookingUpdateRequestDTO);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking patchBooking(Integer id, BookingPatchRequestDTO bookingPatchRequestDTO) {
        Booking booking = getBookingById(id);

        // Validate number of guests
        if (bookingPatchRequestDTO.getNumberOfGuests().isExplicitlySet()) {
            if (bookingPatchRequestDTO.getNumberOfGuests().getValue() > booking.getRoom().getRoomType().getMaxCapacity()) {
                throw new IllegalArgumentException("Number of guests exceeds max capacity of room type");
            }
        }

        // Patch Booking
        EntityPatcher.patchEntity(booking, bookingPatchRequestDTO);

        return bookingRepository.save(booking);
    }

    @Transactional
    public void deleteBooking(Integer id) {
        Booking booking = getBookingById(id);

        // Check if booking is still in progress
        if (booking.getStatus() == BookingStatus.CONFIRMED || booking.getStatus() == BookingStatus.PENDING) {
            throw new IllegalStateException("Cannot delete booking with status ' " + booking.getStatus() + " ' ");
        }

        bookingRepository.deleteById(id);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */

    /* Helper methods ---------------------------------------------------------------------------------------------- */

    private void validateCreateRequestDTO(BookingCreateRequestDTO bookingCreateRequestDTO, Room room) {
        // Check if property is active
        if (!room.getRoomType().getProperty().isActive()) {
            throw new IllegalStateException("Property is not active");
        }

        // Check if room is active
        if (!room.isActive()) {
            throw new IllegalStateException("Room is not active");
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
            throw new IllegalStateException("Room is already booked for the selected dates");
        }
    }

    private double calculateTotalPrice(double pricePerNight, LocalDate checkInDate, LocalDate checkOutDate) {
        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        return pricePerNight * days;
    }

    public boolean existsBookingsForProperty(Integer propertyId) {
        return bookingRepository.existsBookingsForProperty(propertyId);
    }

    public boolean existsOngoingBookingsForProperty(Integer propertyId) {
        return bookingRepository.existsOngoingBookingsForProperty(propertyId);
    }

    public boolean existsBookingsForRoomType(Integer roomTypeId) {
        return bookingRepository.existsBookingsForRoomType(roomTypeId);
    }

    public boolean existsOngoingBookingsForRoomType(Integer roomTypeId) {
        return bookingRepository.existsOngoingBookingsForRoomType(roomTypeId);
    }

    public boolean existsBookingsForRoom(Integer roomId) {
        return bookingRepository.existsBookingsForRoom(roomId);
    }

    public boolean existsOngoingBookingsForRoom(Integer roomId) {
        return bookingRepository.existsOngoingBookingsForRoom(roomId);
    }

    public boolean existsBookingsForBed(Integer bedId) {
        return bookingRepository.existsBookingsForBed(bedId);
    }

    public boolean existsOngoingBookingsForBed(Integer bedId) {
        return bookingRepository.existsOngoingBookingsForBed(bedId);
    }

    public boolean existsBookingsForUser(Integer userId) {
        return bookingRepository.existsBookingsForUser(userId);
    }

    public boolean existsOngoingBookingsForUser(Integer userId) {
        return bookingRepository.existsOngoingBookingsForUser(userId);
    }
}
