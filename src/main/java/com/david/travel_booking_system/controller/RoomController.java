package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.RoomBasicDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.response.full.RoomFullDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.RoomPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.RoomUpdateRequestDTO;
import com.david.travel_booking_system.mapper.RoomMapper;
import com.david.travel_booking_system.service.RoomService;
import jakarta.validation.Valid;
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
    private final RoomMapper roomMapper;

    @Autowired
    public RoomController(RoomService roomService, RoomMapper roomMapper) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<RoomBasicDTO> createRoom(@RequestBody @Valid RoomCreateRequestDTO roomCreateRequestDTO) {
        RoomBasicDTO createdRoom = roomMapper.toBasicDTO(roomService.createRoom(roomCreateRequestDTO));
        return ResponseEntity.status(201).body(createdRoom); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<RoomBasicDTO>> getRooms() {
        List<RoomBasicDTO> rooms = roomMapper.toBasicDTOs(roomService.getRooms());
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomFullDTO> getRoom(@PathVariable Integer id) {
        RoomFullDTO room = roomMapper.toFullDTO(roomService.getRoomById(id));
        return ResponseEntity.ok(room); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    public ResponseEntity<RoomBasicDTO> updateRoom(
            @PathVariable Integer id,
            @RequestBody @Valid RoomUpdateRequestDTO roomUpdateRequestDTO
    ) {
        RoomBasicDTO updatedRoom = roomMapper.toBasicDTO(roomService.updateRoom(id, roomUpdateRequestDTO));
        return ResponseEntity.ok(updatedRoom); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PatchMapping("/{id}")
    public ResponseEntity<RoomBasicDTO> patchRoom(
            @PathVariable Integer id,
            @RequestBody @Valid RoomPatchRequestDTO roomPatchRequestDTO
    ) {
        RoomBasicDTO patchedRoom = roomMapper.toBasicDTO(roomService.patchRoom(id, roomPatchRequestDTO));
        return ResponseEntity.ok(patchedRoom); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
