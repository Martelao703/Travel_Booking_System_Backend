package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.RoomBasicDTO;
import com.david.travel_booking_system.dto.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.full.RoomFullDTO;
import com.david.travel_booking_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/room")
public class RoomController {
    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<RoomBasicDTO> createRoom(@RequestBody RoomCreateRequestDTO roomCreateRequestDTO) {
        RoomBasicDTO createdRoom = RoomBasicDTO.from(roomService.createRoom(roomCreateRequestDTO));
        return ResponseEntity.status(201).body(createdRoom); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<RoomBasicDTO>> getRooms() {
        List<RoomBasicDTO> rooms = RoomBasicDTO.from(roomService.getRooms());
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomFullDTO> getRoom(@PathVariable Integer id) {
        RoomFullDTO room = RoomFullDTO.from(roomService.getRoomById(id));
        return ResponseEntity.ok(room); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
