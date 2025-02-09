package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.dto.request.patchRequest.RoomTypePatchRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.RoomTypeUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.RoomTypePatchFieldRules;
import com.david.travel_booking_system.mapper.RoomTypeMapper;
import com.david.travel_booking_system.model.Property;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomTypeRepository;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final PropertyService propertyService;
    private final BookingService bookingService;
    private final RoomTypeMapper roomTypeMapper;

    @Autowired
    public RoomTypeService(RoomTypeRepository roomTypeRepository, PropertyService propertyService,
                           BookingService bookingService, RoomTypeMapper roomTypeMapper) {
        this.roomTypeRepository = roomTypeRepository;
        this.propertyService = propertyService;
        this.bookingService = bookingService;
        this.roomTypeMapper = roomTypeMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public RoomType createRoomType(RoomTypeCreateRequestDTO roomTypeCreateRequestDTO) {
        // Find associated Property
        Property property = propertyService.getPropertyById(roomTypeCreateRequestDTO.getPropertyId());

        // Create RoomType from DTO
        RoomType roomType = roomTypeMapper.createRoomTypeFromDTO(roomTypeCreateRequestDTO);

        // Add the new RoomType to its Property's roomTypes list
        property.getRoomTypes().add(roomType);

        return roomTypeRepository.save(roomType);
    }

    @Transactional(readOnly = true)
    public List<RoomType> getRoomTypes() {
        return roomTypeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public RoomType getRoomTypeById(Integer id) {
        return roomTypeRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("RoomType with ID " + id + " not found"));
    }

    @Transactional
    public RoomType updateRoomType(Integer id, RoomTypeUpdateRequestDTO roomTypeUpdateRequestDTO) {
        RoomType roomType = getRoomTypeById(id);

        // Check if room type has any booked rooms
        boolean hasBookings = bookingService.existsBookingsForRoomType(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot update a room type with booked rooms");
        }

        // Update RoomType from DTO
        roomTypeMapper.updateRoomTypeFromDTO(roomType, roomTypeUpdateRequestDTO);

        return roomTypeRepository.save(roomType);
    }

    @Transactional
    public RoomType patchRoomType(Integer id, RoomTypePatchRequestDTO roomTypePatchRequestDTO) {
        RoomType roomType = getRoomTypeById(id);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !RoomTypePatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !RoomTypePatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = bookingService.existsBookingsForRoomType(id);
            if (hasBookings && !RoomTypePatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = bookingService.existsOngoingBookingsForRoomType(id);
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
    public void deleteRoomType(Integer id) {
        RoomType roomType = getRoomTypeById(id);

        // Check if room type has any booked rooms
        boolean hasBookings = bookingService.existsBookingsForRoomType(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a room type with booked rooms");
        }

        roomTypeRepository.delete(roomType);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */

    /* Helper methods ---------------------------------------------------------------------------------------------- */
}