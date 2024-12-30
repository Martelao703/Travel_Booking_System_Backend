package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.RoomUpdateRequestDTO;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomTypeService roomTypeService;
    private final BookingService bookingService;

    @Autowired
    public RoomService(RoomRepository roomRepository, RoomTypeService roomTypeService, BookingService bookingService) {
        this.roomRepository = roomRepository;
        this.roomTypeService = roomTypeService;
        this.bookingService = bookingService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Room createRoom(RoomCreateRequestDTO roomCreateRequestDTO) {
        // Find associated RoomType
        RoomType roomType = roomTypeService.getRoomTypeById(roomCreateRequestDTO.getRoomTypeId());

        // Build Room object from DTO
        Room room = new Room(roomType, roomCreateRequestDTO.getFloorNumber(), roomCreateRequestDTO.isAvailable(),
                roomCreateRequestDTO.isCleaned(), roomCreateRequestDTO.isUnderMaintenance());

        // Add the new Room to its RoomType's rooms list
        roomType.getRooms().add(room);

        // Save Room
        return roomRepository.save(room);
    }

    @Transactional
    public List<Room> createRooms(List<RoomCreateRequestDTO> roomCreateRequestDTOs) {
        return roomCreateRequestDTOs.stream()
                .map(this::createRoom)
                .collect(Collectors.toList());
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

        // Update fields
        room.setFloorNumber(roomUpdateRequestDTO.getFloorNumber());
        room.setActive(roomUpdateRequestDTO.isActive());
        room.setAvailable(roomUpdateRequestDTO.isAvailable());
        room.setCleaned(roomUpdateRequestDTO.isCleaned());
        room.setUnderMaintenance(roomUpdateRequestDTO.isUnderMaintenance());

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

        // Delete Room
        roomRepository.delete(room);
    }

    /* Helper methods ---------------------------------------------------------------------------------------------- */
}
