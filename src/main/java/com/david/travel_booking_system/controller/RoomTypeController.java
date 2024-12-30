package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.dto.detail.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.request.RoomTypeRequestDTO;
import com.david.travel_booking_system.dto.full.RoomTypeFullDTO;
import com.david.travel_booking_system.service.RoomTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/room-type")
public class RoomTypeController {
    private final RoomTypeService roomTypeService;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService) {
        this.roomTypeService = roomTypeService;
    }

    /* Receives a general RequestDTO instead of a specific CreateRequestDTO due to the entity not needing specific request DTOs*/
    @PostMapping
    public ResponseEntity<RoomTypeDetailDTO> createRoomType(@RequestBody @Valid RoomTypeRequestDTO roomTypeRequestDTO) {
        RoomTypeDetailDTO createdRoomType = RoomTypeDetailDTO.from(roomTypeService.createRoomType(roomTypeRequestDTO));
        return ResponseEntity.status(201).body(createdRoomType); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeBasicDTO>> getRoomTypes() {
        List<RoomTypeBasicDTO> roomTypes = RoomTypeBasicDTO.from(roomTypeService.getRoomTypes());
        return ResponseEntity.ok(roomTypes); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeFullDTO> getRoomType(@PathVariable Integer id) {
        RoomTypeFullDTO roomType = RoomTypeFullDTO.from(roomTypeService.getRoomTypeById(id));
        return ResponseEntity.ok(roomType); // Return 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDetailDTO> updateRoomType(@PathVariable Integer id,
                                                            @RequestBody @Valid RoomTypeRequestDTO roomTypeRequestDTO) {
        RoomTypeDetailDTO updatedRoomType = RoomTypeDetailDTO.from(roomTypeService.updateRoomType(id, roomTypeRequestDTO));
        return ResponseEntity.ok(updatedRoomType); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
