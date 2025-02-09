package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.request.patchRequest.RoomPatchRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.RoomUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.RoomPatchFieldRules;
import com.david.travel_booking_system.mapper.RoomMapper;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomRepository;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomTypeService roomTypeService;
    private final BookingService bookingService;
    private final RoomMapper roomMapper;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomTypeService roomTypeService, BookingService bookingService,
                       RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomTypeService = roomTypeService;
        this.bookingService = bookingService;
        this.roomMapper = roomMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Room createRoom(RoomCreateRequestDTO roomCreateRequestDTO) {
        // Find associated RoomType
        RoomType roomType = roomTypeService.getRoomTypeById(roomCreateRequestDTO.getRoomTypeId());

        // Create Room from DTO
        Room room = roomMapper.createRoomFromDTO(roomCreateRequestDTO);

        // Add the new Room to its RoomType's rooms list
        roomType.getRooms().add(room);

        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public List<Room> getRooms() {
        return roomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Room getRoomById(Integer id) {
        return roomRepository.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Room with id " + id + " not found"));
    }

    @Transactional
    public Room updateRoom(Integer id, RoomUpdateRequestDTO roomUpdateRequestDTO) {
        Room room = getRoomById(id);

        // Check if room has bookings
        boolean hasBookings = bookingService.existsBookingsForRoom(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot update a room with bookings");
        }

        // Update Room from DTO
        roomMapper.updateRoomFromDTO(room, roomUpdateRequestDTO);

        return roomRepository.save(room);
    }

    @Transactional
    public Room patchRoom(Integer id, RoomPatchRequestDTO roomPatchRequestDTO) {
        Room room = getRoomById(id);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !RoomPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !RoomPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = bookingService.existsBookingsForRoom(id);
            if (hasBookings && !RoomPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = bookingService.existsOngoingBookingsForRoom(id);
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
    public void deleteRoom(Integer id) {
        Room room = getRoomById(id);

        // Check if room has bookings
        boolean hasBookings = bookingService.existsBookingsForRoom(id);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete a room with bookings");
        }

        roomRepository.delete(room);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */

    /* Helper methods ---------------------------------------------------------------------------------------------- */
}
