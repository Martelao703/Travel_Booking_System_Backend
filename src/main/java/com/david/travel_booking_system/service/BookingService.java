package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.specialized.BookingDateChangeRequestDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.BookingPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.BookingUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.mapper.BookingMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.repository.RoomRepository;
import com.david.travel_booking_system.repository.UserRepository;
import com.david.travel_booking_system.specification.BookingSpecifications;
import com.david.travel_booking_system.util.BookingServiceHelper;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {
    // Repositories
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    // Mappers
    private final BookingMapper bookingMapper;

    // Helpers
    private final BookingServiceHelper bookingServiceHelper;

    @Autowired
    public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository,
                          UserRepository userRepository, RoomRepository roomRepository, BookingMapper bookingMapper,
                          BookingServiceHelper bookingServiceHelper) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.bookingMapper = bookingMapper;
        this.bookingServiceHelper = bookingServiceHelper;
    }

    /* CRUD and Basic Methods -------------------------------------------------------------------------------------- */

    @Transactional
    public Booking createBooking(BookingCreateRequestDTO bookingCreateRequestDTO) {
        Integer userId = bookingCreateRequestDTO.getUserId();
        Integer roomId = bookingCreateRequestDTO.getRoomId();

        // Ensure the User and Room exist and are not soft-deleted
        Specification<User> userSpec = BaseSpecifications.filterById(User.class, userId)
                .and(BaseSpecifications.excludeDeleted(User.class));
        Specification<Room> roomSpec = BaseSpecifications.filterById(Room.class, roomId)
                .and(BaseSpecifications.excludeDeleted(Room.class));

        // Retrieve the User and Room
        User user = userRepository.findOne(userSpec)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));
        Room room = roomRepository.findOne(roomSpec)
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + roomId + " not found"));

        bookingServiceHelper.validateCreateRequestDTO(bookingCreateRequestDTO, room, user);

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
    public List<Booking> getBookings(boolean includeDeleted) {
        Specification<Booking> spec = includeDeleted
                ? Specification.where(null)  // No spec
                : BaseSpecifications.excludeDeleted(Booking.class); // Non-deleted filter

        return bookingRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Booking getBookingById(Integer bookingId, boolean includeDeleted) {
        Specification<Booking> spec = includeDeleted
                ? BaseSpecifications.filterById(Booking.class, bookingId) // ID filter
                : BaseSpecifications.filterById(Booking.class, bookingId)
                .and(BaseSpecifications.excludeDeleted(Booking.class)); // ID and non-deleted filter

        return bookingRepository.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("Booking with ID " + bookingId + " not found"));
    }

    @Transactional
    public Booking updateBooking(Integer id, BookingUpdateRequestDTO bookingUpdateRequestDTO) {
        Booking booking = getBookingById(id, false);

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
        Booking booking = getBookingById(id, false);

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
    public void softDeleteBooking(Integer id) {
        Booking booking = getBookingById(id, false);

        // Check if booking is still in progress
        if (booking.getStatus() == BookingStatus.CONFIRMED || booking.getStatus() == BookingStatus.PENDING
                || booking.getStatus() == BookingStatus.ONGOING) {
            throw new IllegalStateException("Cannot delete booking with status ' " + booking.getStatus() + " ' ");
        }

        // Soft delete the booking
        booking.setDeleted(true);
        bookingRepository.save(booking);
    }

    @Transactional
    public void hardDeleteBooking(Integer id) {
        Booking booking = getBookingById(id, true);

        // Check if booking is not soft-deleted
        if (booking.isDeleted()) {
            // Check if booking is still in progress
            if (booking.getStatus() == BookingStatus.CONFIRMED || booking.getStatus() == BookingStatus.PENDING
                    || booking.getStatus() == BookingStatus.ONGOING) {
                throw new IllegalStateException("Cannot delete booking with status ' " + booking.getStatus() + " ' ");
            }
        }

        // Hard delete the booking
        bookingRepository.deleteById(id);
    }

    @Transactional
    public void restoreBooking(Integer id) {
        Booking booking = getBookingById(id, true);

        // Check if booking is soft-deleted
        if (!booking.isDeleted()) {
            throw new IllegalStateException("Booking is not deleted");
        }

        // Restore the booking
        booking.setDeleted(false);
        bookingRepository.save(booking);
    }

    /* Get Lists of Nested Entities */

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByRoomId(Integer roomId, boolean includeDeleted) {
        // Ensure the room exists and is not soft-deleted
        Specification<Room> roomSpec = BaseSpecifications.filterById(Room.class, roomId)
                .and(BaseSpecifications.excludeDeleted(Room.class));

        if (!roomRepository.exists(roomSpec)) {
            throw new EntityNotFoundException("Room with ID " + roomId + " not found");
        }

        // Filter by room ID
        Specification<Booking> bookingSpec = includeDeleted
                ? BookingSpecifications.filterByRoomId(roomId) // Room ID filter
                : BookingSpecifications.filterByRoomId(roomId)
                .and(BaseSpecifications.excludeDeleted(Booking.class)); // Room ID and non-deleted filter

        return bookingRepository.findAll(bookingSpec);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByPropertyId(Integer propertyId, boolean includeDeleted) {
        // Ensure the property exists and is not soft-deleted
        Specification<Property> propertySpec = BaseSpecifications.filterById(Property.class, propertyId)
                .and(BaseSpecifications.excludeDeleted(Property.class));

        if (!propertyRepository.exists(propertySpec)) {
            throw new EntityNotFoundException("Property with ID " + propertyId + " not found");
        }

        // Filter by property ID
        Specification<Booking> bookingSpec = includeDeleted
                ? BookingSpecifications.filterByPropertyId(propertyId) // Property ID filter
                : BookingSpecifications.filterByPropertyId(propertyId)
                .and(BaseSpecifications.excludeDeleted(Booking.class)); // Property ID and non-deleted filter

        return bookingRepository.findAll(bookingSpec);
    }

    @Transactional(readOnly = true)
    public List<Booking> getBookingsByUserId(Integer userId, boolean includeDeleted) {
        // Ensure the user exists and is not soft-deleted
        Specification<User> userSpec = BaseSpecifications.filterById(User.class, userId)
                .and(BaseSpecifications.excludeDeleted(User.class));

        if (!userRepository.exists(userSpec)) {
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }

        // Filter by user ID
        Specification<Booking> bookingSpec = includeDeleted
                ? BookingSpecifications.filterByUserId(userId) // User ID filter
                : BookingSpecifications.filterByUserId(userId)
                .and(BaseSpecifications.excludeDeleted(Booking.class)); // User ID and non-deleted filter

        return bookingRepository.findAll(bookingSpec);
    }

    /* Custom methods ---------------------------------------------------------------------------------------------- */

    @Transactional
    public Booking changeBookingDates(Integer id, BookingDateChangeRequestDTO bookingDateChangeRequestDTO) {
        Booking booking = getBookingById(id, false);

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
        Booking booking = getBookingById(id, false);

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
        Booking booking = getBookingById(id, false);

        // Validate check-in request
        bookingServiceHelper.validateCheckInRequestDTO(booking);

        booking.setStatus(BookingStatus.ONGOING);
        booking.getRoom().setOccupied(true);
        booking.setActualCheckInDateTime(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking checkOut(Integer id) {
        Booking booking = getBookingById(id, false);

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
        Booking booking = getBookingById(id, false);

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
        Booking booking = getBookingById(id, false);

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
