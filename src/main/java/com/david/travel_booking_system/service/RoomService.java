package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.RoomDTO;
import com.david.travel_booking_system.model.Room;
import com.david.travel_booking_system.model.RoomType;
import com.david.travel_booking_system.repository.RoomRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public Room createRoom(@Valid RoomDTO roomDTO, RoomType roomType) {
        // Build Bed object from DTO
        Room room = new Room(roomType, roomDTO.getFloorNumber(), roomDTO.isAvailable(), roomDTO.isCleaned(),
                roomDTO.isUnderMaintenance());

        return roomRepository.save(room);
    }

    @Transactional
    public List<Room> createRooms(List<RoomDTO> roomDTOs, RoomType roomType) {
        return roomDTOs.stream()
                .map(roomDTO -> createRoom(roomDTO, roomType))
                .collect(Collectors.toList());
    }
}
