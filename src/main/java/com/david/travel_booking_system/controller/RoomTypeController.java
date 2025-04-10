package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.BedBasicDTO;
import com.david.travel_booking_system.dto.response.basic.RoomBasicDTO;
import com.david.travel_booking_system.dto.response.basic.RoomTypeBasicDTO;
import com.david.travel_booking_system.dto.response.detail.RoomTypeDetailDTO;
import com.david.travel_booking_system.dto.response.full.RoomTypeFullDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.RoomTypeCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.RoomTypePatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.RoomTypeUpdateRequestDTO;
import com.david.travel_booking_system.mapper.BedMapper;
import com.david.travel_booking_system.mapper.RoomMapper;
import com.david.travel_booking_system.mapper.RoomTypeMapper;
import com.david.travel_booking_system.service.BedService;
import com.david.travel_booking_system.service.RoomService;
import com.david.travel_booking_system.service.RoomTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {
    // Services
    private final RoomTypeService roomTypeService;
    private final RoomService roomService;
    private final BedService bedService;

    // Mappers
    private final RoomTypeMapper roomTypeMapper;
    private final RoomMapper roomMapper;
    private final BedMapper bedMapper;

    @Autowired
    public RoomTypeController(RoomTypeService roomTypeService, RoomService roomService, BedService bedService,
                              RoomTypeMapper roomTypeMapper, RoomMapper roomMapper, BedMapper bedMapper) {
        this.roomTypeService = roomTypeService;
        this.roomService = roomService;
        this.bedService = bedService;
        this.roomTypeMapper = roomTypeMapper;
        this.roomMapper = roomMapper;
        this.bedMapper = bedMapper;
    }

    /* CRUD and Basic Endpoints ------------------------------------------------------------------------------------ */

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

    @PreAuthorize("hasRole('ROLE_HOST')")
    @GetMapping
    public ResponseEntity<List<RoomTypeBasicDTO>> getRoomTypes() {
        List<RoomTypeBasicDTO> roomTypes = roomTypeMapper.toBasicDTOs(roomTypeService.getRoomTypes(false));
        return ResponseEntity.ok(roomTypes); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeFullDTO> getRoomType(@PathVariable Integer id) {
        RoomTypeFullDTO roomType = roomTypeMapper.toFullDTO(roomTypeService.getRoomTypeById(id, false));
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
        roomTypeService.softDeleteRoomType(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteRoomType(@PathVariable Integer id) {
        roomTypeService.hardDeleteRoomType(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRoomType(@PathVariable Integer id) {
        roomTypeService.restoreRoomType(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Get Lists of Nested Entities */

    @GetMapping("/{id}/rooms")
    public ResponseEntity<List<RoomBasicDTO>> getRooms(@PathVariable Integer id) {
        List<RoomBasicDTO> rooms = roomMapper.toBasicDTOs(roomService.getRoomsByRoomTypeId(id, false));
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}/beds")
    public ResponseEntity<List<BedBasicDTO>> getBeds(@PathVariable Integer id) {
        List<BedBasicDTO> beds = bedMapper.toBasicDTOs(bedService.getBedsByRoomTypeId(id, false));
        return ResponseEntity.ok(beds); // Return 200 OK
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */


}
