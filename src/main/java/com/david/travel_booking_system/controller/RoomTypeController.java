package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.dto.detail.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.full.RoomTypeFullDTO;
import com.david.travel_booking_system.dto.request.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.dto.request.patchRequest.RoomTypePatchRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.RoomTypeUpdateRequestDTO;
import com.david.travel_booking_system.mapper.RoomTypeMapper;
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
    private final RoomTypeMapper roomTypeMapper;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService, RoomTypeMapper roomTypeMapper) {
        this.roomTypeService = roomTypeService;
        this.roomTypeMapper = roomTypeMapper;
    }

    /* Receives a general RequestDTO instead of a specific CreateRequestDTO due to the entity not needing specific request DTOs*/
    @PostMapping
    public ResponseEntity<RoomTypeDetailDTO> createRoomType(
            @RequestBody @Valid RoomTypeCreateRequestDTO roomTypeCreateRequestDTO
    ) {
        RoomTypeDetailDTO createdRoomType = roomTypeMapper.toDetailDTO(
                roomTypeService.createRoomType(roomTypeCreateRequestDTO)
        );
        return ResponseEntity.status(201).body(createdRoomType); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<RoomTypeBasicDTO>> getRoomTypes() {
        List<RoomTypeBasicDTO> roomTypes = roomTypeMapper.toBasicDTOs(roomTypeService.getRoomTypes());
        return ResponseEntity.ok(roomTypes); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeFullDTO> getRoomType(@PathVariable Integer id) {
        RoomTypeFullDTO roomType = roomTypeMapper.toFullDTO(roomTypeService.getRoomTypeById(id));
        return ResponseEntity.ok(roomType); // Return 200 OK
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomTypeDetailDTO> updateRoomType(
            @PathVariable Integer id,
            @RequestBody @Valid RoomTypeUpdateRequestDTO roomTypeUpdateRequestDTO
    ) {
        RoomTypeDetailDTO updatedRoomType = roomTypeMapper.toDetailDTO(
                roomTypeService.updateRoomType(id, roomTypeUpdateRequestDTO)
        );
        return ResponseEntity.ok(updatedRoomType); // Return 200 OK
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoomTypeDetailDTO> patchRoomType(
            @PathVariable Integer id,
            @RequestBody @Valid RoomTypePatchRequestDTO roomTypePatchRequestDTO
    ) {
        RoomTypeDetailDTO patchedRoomType = roomTypeMapper.toDetailDTO(
                roomTypeService.patchRoomType(id, roomTypePatchRequestDTO)
        );
        return ResponseEntity.ok(patchedRoomType); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
