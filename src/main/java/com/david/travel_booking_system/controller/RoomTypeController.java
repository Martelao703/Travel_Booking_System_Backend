package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.RoomTypeDTO;
import com.david.travel_booking_system.dto.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    @PostMapping
    public ResponseEntity<RoomTypeDetailDTO> createRoomType(@RequestBody RoomTypeCreateRequestDTO roomTypeCreateRequestDTO) {
        RoomTypeDetailDTO createdRoomType = RoomTypeDetailDTO.from(roomTypeService.createRoomType(roomTypeCreateRequestDTO));
        return ResponseEntity.status(201).body(createdRoomType); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeDTO>> getRoomTypes() {
        List<RoomTypeDTO> roomTypes = RoomTypeDTO.from(roomTypeService.getRoomTypes());
        return ResponseEntity.ok(roomTypes); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDetailDTO> getRoomType(@PathVariable Integer id) {
        RoomTypeDetailDTO roomType = RoomTypeDetailDTO.from(roomTypeService.getRoomTypeById(id));
        return ResponseEntity.ok(roomType); // Return 200 OK
    }
}
