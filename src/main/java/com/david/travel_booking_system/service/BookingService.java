package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.specialized.BookingDateChangeRequestDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.BookingPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.BookingUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.mapper.BookingMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.util.BookingServiceHelper;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final RoomService roomService;
    private final BookingMapper bookingMapper;
    private final BookingServiceHelper bookingServiceHelper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserService userService, RoomService roomService,
                          BookingMapper bookingMapper, BookingServiceHelper bookingServiceHelper) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.roomService = roomService;
        this.bookingMapper = bookingMapper;
        this.bookingServiceHelper = bookingServiceHelper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Booking createBooking(BookingCreateRequestDTO bookingCreateRequestDTO) {
        // Find associated User and Room
        User user = userService.getUserById(bookingCreateRequestDTO.getUserId());
        Room room = roomService.getRoomById(bookingCreateRequestDTO.getRoomId());

        bookingServiceHelper.validateCreateRequestDTO(bookingCreateRequestDTO, room);

        // Create Booking from DTO
        Booking booking = bookingMapper.createBookingFromDTO(bookingCreateRequestDTO);

        // Calculate total price and set it on the Booking entity
        booking.setTotalPrice(
                bookingServiceHelper.calculateTotalPrice(
                        room.getRoomType().getPricePerNight(), bookingCreateRequestDTO.getPlannedCheckInDateTime(),
                        bookingCreateRequestDTO.getPlannedCheckOutDateTime()
                )
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

    /* Service custom endpoint methods ----------------------------------------------------------------------------- */

    @Transactional
    public Booking changeBookingDates(Integer id, BookingDateChangeRequestDTO bookingDateChangeRequestDTO) {
        Booking booking = getBookingById(id);

        // Validate date change request
        bookingServiceHelper.validateBookingDateChange(booking, bookingDateChangeRequestDTO);

        // Apply new dates if explicitly set
        bookingDateChangeRequestDTO.getPlannedCheckInDateTime().ifExplicitlySet(booking::setPlannedCheckInDateTime);
        bookingDateChangeRequestDTO.getPlannedCheckOutDateTime().ifExplicitlySet(booking::setPlannedCheckOutDateTime);

        // Recalculate total price
        booking.setTotalPrice(
                bookingServiceHelper.calculateTotalPrice(
                        booking.getRoom().getRoomType().getPricePerNight(),
                        booking.getPlannedCheckInDateTime(),
                        booking.getPlannedCheckOutDateTime()
                )
        );

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking confirmPayment(Integer id) {
        Booking booking = getBookingById(id);

        // Check if booking is already paid
        if (booking.isPaid()) {
            throw new IllegalStateException("Booking is already paid");
        }

        // Check if booking is still pending
        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new IllegalStateException("Payment can only be confirmed for bookings with 'PENDING' status");
        }

        booking.setPaid(true);
        booking.setStatus(BookingStatus.CONFIRMED);

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkIn(Integer id) {
        Booking booking = getBookingById(id);

        // Validate check-in request
        bookingServiceHelper.validateCheckInRequestDTO(booking);

        booking.setStatus(BookingStatus.ONGOING);
        booking.getRoom().setOccupied(true);
        booking.setActualCheckInDateTime(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkOut(Integer id) {
        Booking booking = getBookingById(id);

        // Check if booking is still ongoing
        if (booking.getStatus() != BookingStatus.ONGOING) {
            throw new IllegalStateException("Check-out only allowed for 'ONGOING' bookings.");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        booking.getRoom().setOccupied(false);
        booking.setActualCheckOutDateTime(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    // User cancels a booking
    @Transactional
    public Booking cancelBooking(Integer id) {
        Booking booking = getBookingById(id);

        // Check if booking is still in progress
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED
                && booking.getStatus() != BookingStatus.ONGOING) {
            throw new IllegalStateException("Only PENDING, CONFIRMED or ONGOING bookings can be cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.getRoom().setOccupied(false);

        return bookingRepository.save(booking);
    }

    // Property/Admin rejects a booking
    @Transactional
    public Booking rejectBooking(Integer id) {
        Booking booking = getBookingById(id);

        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.CONFIRMED
                && booking.getStatus() != BookingStatus.ONGOING) {
            throw new IllegalStateException("Only PENDING, CONFIRMED or ONGOING bookings can be rejected.");
        }

        /*
        if (booking.getStatus() == BookingStatus.CONFIRMED || booking.getStatus() == BookingStatus.ONGOING) {
            TODO - Refund user
        }
        */

        booking.setStatus(BookingStatus.REJECTED);
        booking.getRoom().setOccupied(false);

        return bookingRepository.save(booking);
    }

    // Automatically mark a booking as expired
    @Transactional
    public void markBookingAsExpired(Booking booking) {
        if (booking.getStatus() == BookingStatus.PENDING) {
            booking.setStatus(BookingStatus.EXPIRED);
            bookingRepository.save(booking);
        }
    }

    // Automatically mark a booking as no-show
    @Transactional
    public void markBookingAsNoShow(Booking booking) {
        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            booking.setStatus(BookingStatus.NO_SHOW);
            bookingRepository.save(booking);
        }
    }

    /* Helper methods ---------------------------------------------------------------------------------------------- */
}
