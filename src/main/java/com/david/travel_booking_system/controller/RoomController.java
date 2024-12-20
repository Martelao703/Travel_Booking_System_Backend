package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.RoomDTO;
import com.david.travel_booking_system.dto.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.detail.RoomDetailDTO;
import com.david.travel_booking_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<RoomDetailDTO> createRoom(@RequestBody RoomCreateRequestDTO roomCreateRequestDTO) {
        RoomDetailDTO createdRoom = RoomDetailDTO.from(roomService.createRoom(roomCreateRequestDTO));
        return ResponseEntity.status(201).body(createdRoom); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<RoomDTO>> getRooms() {
        List<RoomDTO> rooms = RoomDTO.from(roomService.getRooms());
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDetailDTO> getRoom(@PathVariable Integer id) {
        RoomDetailDTO room = RoomDetailDTO.from(roomService.getRoomById(id));
        return ResponseEntity.ok(room); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
