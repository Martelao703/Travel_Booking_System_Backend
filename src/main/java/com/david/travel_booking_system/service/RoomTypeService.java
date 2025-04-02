package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.crud.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.RoomTypePatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.RoomTypeUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.RoomTypePatchFieldRules;
import com.david.travel_booking_system.mapper.RoomTypeMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.*;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.specification.BookingSpecifications;
import com.david.travel_booking_system.specification.RoomTypeSpecifications;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomTypeService {
    // Repositories
    private final RoomTypeRepository roomTypeRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;

    // Mapper
    private final RoomTypeMapper roomTypeMapper;
    private final BedRepository bedRepository;
    private final RoomRepository roomRepository;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, BookingRepository bookingRepository,
                           PropertyRepository propertyRepository, RoomTypeMapper roomTypeMapper, BedRepository bedRepository, RoomRepository roomRepository) {
        this.roomTypeRepository = roomTypeRepository;
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.roomTypeMapper = roomTypeMapper;
        this.bedRepository = bedRepository;
        this.roomRepository = roomRepository;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public RoomType createRoomType(RoomTypeCreateRequestDTO roomTypeCreateRequestDTO) {
        Integer propertyId = roomTypeCreateRequestDTO.getPropertyId();

        // Ensure the property exists and is not soft-deleted
        Specification<Property> propertySpec = BaseSpecifications.filterById(Property.class, propertyId)
                .and(BaseSpecifications.excludeDeleted(Property.class));

        // Retrieve the property
        Property property = propertyRepository.findOne(propertySpec).
                orElseThrow(() -> new EntityNotFoundException("Property with ID " + propertyId + " not found"));

        // Create RoomType from DTO
        RoomType roomType = roomTypeMapper.createRoomTypeFromDTO(roomTypeCreateRequestDTO);

        // Add the new RoomType to its Property's roomTypes list
        property.getRoomTypes().add(roomType);

        return roomTypeRepository.save(roomType);
    }

    @Transactional(readOnly = true)
    public List<RoomType> getRoomTypes(boolean includeDeleted) {
        Specification<RoomType> spec = includeDeleted
                ? Specification.where(null)  // No spec
                : BaseSpecifications.excludeDeleted(RoomType.class); // Non-deleted filter

        return roomTypeRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public RoomType getRoomTypeById(Integer id, boolean includeDeleted) {
        Specification<RoomType> spec = includeDeleted
                ? BaseSpecifications.filterById(RoomType.class, id) // ID filter
                : BaseSpecifications.filterById(RoomType.class, id)
                .and(BaseSpecifications.excludeDeleted(RoomType.class)); // ID and non-deleted filter

        return roomTypeRepository.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("RoomType with ID " + id + " not found"));
    }

    @Transactional
    public RoomType updateRoomType(Integer id, RoomTypeUpdateRequestDTO roomTypeUpdateRequestDTO) {
        RoomType roomType = getRoomTypeById(id, false);

        // Check if room type has any booked rooms
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot update a room type with booked rooms");
        }

        // Update RoomType from DTO
        roomTypeMapper.updateRoomTypeFromDTO(roomType, roomTypeUpdateRequestDTO);

        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public RoomType patchRoomType(Integer id, RoomTypePatchRequestDTO roomTypePatchRequestDTO) {
        RoomType roomType = getRoomTypeById(id, false);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !RoomTypePatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !RoomTypePatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = hasActiveBookings(id);
            if (hasBookings && !RoomTypePatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = hasOngoingBookings(id);
            }
        }

        // Validate and patch RoomType
        EntityPatcher.validateAndPatchEntity(
                roomType,
                roomTypePatchRequestDTO,
                RoomTypePatchFieldRules.CRITICAL_FIELDS,
                RoomTypePatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS,
                hasBookings,
                hasOngoingBookings
        );

        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public void softDeleteRoomType(Integer id) {
        RoomType roomType = getRoomTypeById(id, false);

        // Check if room type has rooms with active bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot delete a room type with booked rooms");
        }

        // Soft delete all linked entities
        bedRepository.softDeleteByRoomTypeId(id);
        roomRepository.softDeleteByRoomTypeId(id);

        // Soft delete the room type
        roomType.setDeleted(true);
        roomTypeRepository.save(roomType);
    }

    @Transactional
    public void hardDeleteRoomType(Integer id) {
        RoomType roomType = getRoomTypeById(id, true);

        // Check if room type is not soft-deleted
        if (!roomType.isDeleted()) {
            // Check if room type has rooms with active bookings
            if (hasActiveBookings(id)) {
                throw new IllegalStateException("Cannot delete a room type with booked rooms");
            }
        }

        // Hard delete room type (CascadeType.ALL will handle Bed and Room deletion)
        roomTypeRepository.deleteById(id);
    }

    /* Custom methods ---------------------------------------------------------------------------------------------- */

    @Transactional(readOnly = true)
    public List<RoomType> getRoomTypesByPropertyId(Integer propertyId, boolean includeDeleted) {
        // Ensure the property exists and is not soft-deleted
        Specification<Property> propertySpec = BaseSpecifications.filterById(Property.class, propertyId)
                .and(BaseSpecifications.excludeDeleted(Property.class));

        if (!propertyRepository.exists(propertySpec)) {
            throw new EntityNotFoundException("Property with ID " + propertyId + " not found");
        }

        // Filter by property ID
        Specification<RoomType> roomTypeSpec = includeDeleted
                ? RoomTypeSpecifications.filterByPropertyId(propertyId) // Property ID filter
                : RoomTypeSpecifications.filterByPropertyId(propertyId)
                .and(BaseSpecifications.excludeDeleted(RoomType.class)); // Property ID and non-deleted filter

        return roomTypeRepository.findAll(roomTypeSpec);
    }

    /* Helper methods ---------------------------------------------------------------------------------------------- */

    private boolean hasActiveBookings(Integer id) {
        // Filter bookings by room type ID and relevant statuses
        Specification<Booking> bookingSpec = BookingSpecifications.filterByRoomTypeId(id)
                .and(BookingSpecifications.filterByStatuses(List.of(
                        BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ONGOING
                )));
        return bookingRepository.exists(bookingSpec);
    }

    private boolean hasOngoingBookings(Integer id) {
        // Filter bookings by room type ID and ONGOING status
        Specification<Booking> bookingSpec = BookingSpecifications.filterByRoomTypeId(id)
                .and(BookingSpecifications.filterByStatus(BookingStatus.ONGOING));

        return bookingRepository.exists(bookingSpec);
    }
}