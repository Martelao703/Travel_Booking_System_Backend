package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.crud.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.RoomPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.RoomUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.RoomPatchFieldRules;
import com.david.travel_booking_system.mapper.RoomMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.repository.PropertyRepository;
import com.david.travel_booking_system.repository.RoomRepository;
import com.david.travel_booking_system.repository.RoomTypeRepository;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.specification.BookingSpecifications;
import com.david.travel_booking_system.specification.RoomSpecifications;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class RoomService {
    // Repositories
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final RoomTypeRepository roomTypeRepository;

    // Mappers
    private final RoomMapper roomMapper;

    @Autowired
    public RoomService(RoomRepository roomRepository, BookingRepository bookingRepository,
                       PropertyRepository propertyRepository, RoomTypeRepository roomTypeRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.roomMapper = roomMapper;
    }

    /* CRUD and Basic Methods -------------------------------------------------------------------------------------- */

    @Transactional
    public Room createRoom(RoomCreateRequestDTO roomCreateRequestDTO) {
        Integer id = roomCreateRequestDTO.getRoomTypeId();

        // Ensure the RoomType exists and is not soft-deleted
        Specification<RoomType> roomTypeSpec = BaseSpecifications.filterById(RoomType.class, id)
                .and(BaseSpecifications.excludeDeleted(RoomType.class));

        // Retrieve the RoomType
        RoomType roomType = roomTypeRepository.findOne(roomTypeSpec)
                .orElseThrow(() -> new EntityNotFoundException("RoomType with ID " + id + " not found"));

        // Create Room from DTO
        Room room = roomMapper.createRoomFromDTO(roomCreateRequestDTO);

        // Set active status equal to Property's active status
        room.setActive(roomType.getProperty().isActive());

        // Add the new Room to its RoomType's rooms list
        roomType.getRooms().add(room);

        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<Room> getRooms(boolean includeDeleted) {
        Specification<Room> spec = includeDeleted
                ? Specification.where(null)  // No spec
                : BaseSpecifications.excludeDeleted(Room.class); // Non-deleted filter

        return roomRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Integer id, boolean includeDeleted) {
        Specification<Room> spec = includeDeleted
                ? BaseSpecifications.filterById(Room.class, id) // ID filter
                : BaseSpecifications.filterById(Room.class, id)
                .and(BaseSpecifications.excludeDeleted(Room.class)); // ID and non-deleted filter

        return roomRepository.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("Room with ID " + id + " not found"));
    }

    @Transactional
    public Room updateRoom(Integer id, RoomUpdateRequestDTO roomUpdateRequestDTO) {
        Room room = getRoomById(id, false);

        // Check if room has bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot update a room with bookings");
        }

        // Update Room from DTO
        roomMapper.updateRoomFromDTO(room, roomUpdateRequestDTO);

        return roomRepository.save(room);
    }

    @Transactional
    public Room patchRoom(Integer id, RoomPatchRequestDTO roomPatchRequestDTO) {
        Room room = getRoomById(id, false);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !RoomPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !RoomPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = hasActiveBookings(id);
            if (hasBookings && !RoomPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = hasOngoingBookings(id);
            }
        }

        // Validate and patch room
        EntityPatcher.validateAndPatchEntity(
                room,
                roomPatchRequestDTO,
                RoomPatchFieldRules.CRITICAL_FIELDS,
                RoomPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS,
                hasBookings,
                hasOngoingBookings
        );

        return roomRepository.save(room);
    }

    @Transactional
    public void softDeleteRoom(Integer id) {
        Room room = getRoomById(id, false);

        // Check if room has active bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot delete a room with bookings");
        }

        // Soft delete room
        room.setActive(false);
        room.setDeleted(true);
        roomRepository.save(room);
    }

    @Transactional
    public void hardDeleteRoom(Integer id) {
        Room room = getRoomById(id, true);

        // Check if room is not soft-deleted
        if (!room.isDeleted()) {
            // Check if room has active bookings
            if (hasActiveBookings(id)) {
                throw new IllegalStateException("Cannot delete a room with bookings");
            }
        }

        // Hard delete room
        roomRepository.deleteById(id);
    }

    @Transactional
    public void restoreRoom(Integer id) {
        Room room = getRoomById(id, true);

        // Check if room is soft-deleted
        if (!room.isDeleted()) {
            throw new IllegalStateException("Room is not deleted");
        }

        // Restore room
        room.setDeleted(false);
        roomRepository.save(room);
    }

    /* Get Lists of Nested Entities */

    @Transactional(readOnly = true)
    public List<Room> getRoomsByRoomTypeId(Integer roomTypeId, boolean includeDeleted) {
        // Ensure the room type exists and is not soft-deleted
        Specification<RoomType> roomTypeSpec = BaseSpecifications.filterById(RoomType.class, roomTypeId)
                .and(BaseSpecifications.excludeDeleted(RoomType.class));

        if (!roomTypeRepository.exists(roomTypeSpec)) {
            throw new EntityNotFoundException("RoomType with ID " + roomTypeId + " not found");
        }

        // Filter by room type ID
        Specification<Room> roomSpec = includeDeleted
                ? RoomSpecifications.filterByRoomTypeId(roomTypeId) // RoomType ID filter
                : RoomSpecifications.filterByRoomTypeId(roomTypeId)
                .and(BaseSpecifications.excludeDeleted(Room.class)); // RoomType ID and non-deleted filter

        return roomRepository.findAll(roomSpec);
    }

    @Transactional(readOnly = true)
    public List<Room> getRoomsByPropertyId(Integer propertyId, boolean includeDeleted) {
        // Ensure the property exists and is not soft-deleted
        Specification<Property> propertySpec = BaseSpecifications.filterById(Property.class, propertyId)
                .and(BaseSpecifications.excludeDeleted(Property.class));

        if (!propertyRepository.exists(propertySpec)) {
            throw new EntityNotFoundException("Property with ID " + propertyId + " not found");
        }

        // Filter by property ID
        Specification<Room> roomSpec = includeDeleted
                ? RoomSpecifications.filterByPropertyId(propertyId) // Property ID filter
                : RoomSpecifications.filterByPropertyId(propertyId)
                .and(BaseSpecifications.excludeDeleted(Room.class)); // Property ID and non-deleted filter

        return roomRepository.findAll(roomSpec);
    }

    /* Custom methods ---------------------------------------------------------------------------------------------- */

    @Transactional
    public void activateRoom(Integer id) {
        Room room = getRoomById(id, true);

        // Check if room is not already active
        if (room.isActive()) {
            throw new IllegalStateException("Room is already active");
        }

        // Set room to active
        room.setActive(true);
        roomRepository.save(room);
    }

    @Transactional
    public void deactivateRoom(Integer id) {
        Room room = getRoomById(id, true);

        // Check if room is not already deactivated
        if (!room.isActive()) {
            throw new IllegalStateException("Room is already deactivated");
        }

        // Check if room has any active bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot deactivate a room with active bookings");
        }

        // Set room to inactive
        room.setActive(false);
        roomRepository.save(room);
    }

    /* Helper methods ---------------------------------------------------------------------------------------------- */

    private boolean hasActiveBookings(Integer id) {
        // Filter bookings by room ID and relevant statuses
        Specification<Booking> bookingSpec = BookingSpecifications.filterByRoomId(id)
                .and(BookingSpecifications.filterByStatuses(List.of(
                        BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ONGOING
                )));
        return bookingRepository.exists(bookingSpec);
    }

    private boolean hasOngoingBookings(Integer id) {
        // Filter bookings by room ID and ONGOING status
        Specification<Booking> bookingSpec = BookingSpecifications.filterByRoomId(id)
                .and(BookingSpecifications.filterByStatus(BookingStatus.ONGOING));
        return bookingRepository.exists(bookingSpec);
    }
}
