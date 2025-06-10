package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.response.basic.RoomBasicDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.RoomCreateRequestDTO;
import com.david.travel_booking_system.dto.response.full.RoomFullDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.RoomPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.RoomUpdateRequestDTO;
import com.david.travel_booking_system.mapper.BookingMapper;
import com.david.travel_booking_system.mapper.RoomMapper;
import com.david.travel_booking_system.service.BookingService;
import com.david.travel_booking_system.service.RoomService;
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
@RequestMapping("/api/rooms")
public class RoomController {
    // Services
    private final RoomService roomService;
    private final BookingService bookingService;

    // Mappers
    private final RoomMapper roomMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public RoomController(RoomService roomService, BookingService bookingService, RoomMapper roomMapper,
                          BookingMapper bookingMapper) {
        this.roomService = roomService;
        this.bookingService = bookingService;
        this.roomMapper = roomMapper;
        this.bookingMapper = bookingMapper;
    }

    /* CRUD and Basic Endpoints ------------------------------------------------------------------------------------ */

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    @PreAuthorize("@roomPermissionChecker.canCreate(authentication, #createDTO.roomTypeId)")
    public ResponseEntity<RoomBasicDTO> createRoom(@RequestBody @Valid RoomCreateRequestDTO createDTO) {
        RoomBasicDTO createdRoom = roomMapper.toBasicDTO(roomService.createRoom(createDTO));
        return ResponseEntity.status(201).body(createdRoom); // Return 201 Created
    }

    @GetMapping
    @PreAuthorize("@roomPermissionChecker.canReadAny(authentication)")
    public ResponseEntity<List<RoomBasicDTO>> getRooms() {
        List<RoomBasicDTO> rooms = roomMapper.toBasicDTOs(roomService.getRooms(false));
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}")
    @PreAuthorize("@roomPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<RoomFullDTO> getRoom(@PathVariable Integer id) {
        RoomFullDTO room = roomMapper.toFullDTO(roomService.getRoomById(id, false));
        return ResponseEntity.ok(room); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    @PreAuthorize("@roomPermissionChecker.canUpdate(authentication, #id)")
    public ResponseEntity<RoomBasicDTO> updateRoom(
            @PathVariable Integer id,
            @RequestBody @Valid RoomUpdateRequestDTO updateDTO
    ) {
        RoomBasicDTO updatedRoom = roomMapper.toBasicDTO(roomService.updateRoom(id, updateDTO));
        return ResponseEntity.ok(updatedRoom); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PatchMapping("/{id}")
    @PreAuthorize("@roomPermissionChecker.canUpdate(authentication, #id)")
    public ResponseEntity<RoomBasicDTO> patchRoom(
            @PathVariable Integer id,
            @RequestBody @Valid RoomPatchRequestDTO patchDTO
    ) {
        RoomBasicDTO patchedRoom = roomMapper.toBasicDTO(roomService.patchRoom(id, patchDTO));
        return ResponseEntity.ok(patchedRoom); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@roomPermissionChecker.canDelete(authentication, #id)")
    public ResponseEntity<Void> deleteRoom(@PathVariable Integer id) {
        roomService.softDeleteRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("@roomPermissionChecker.canDelete(authentication, #id)")
    public ResponseEntity<Void> hardDeleteRoom(@PathVariable Integer id) {
        roomService.hardDeleteRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("@roomPermissionChecker.canRestore(authentication, #id)")
    public ResponseEntity<Void> restoreRoom(@PathVariable Integer id) {
        roomService.restoreRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Get Lists of Nested Entities */

    @GetMapping("/{id}/bookings")
    @PreAuthorize("@roomPermissionChecker.canReadBookings(authentication, #id)")
    public ResponseEntity<List<BookingBasicDTO>> getBookings(@PathVariable Integer id) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByRoomId(id, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */

    @PatchMapping("/{id}/activate")
    @PreAuthorize("@roomPermissionChecker.canActivate(authentication, #id)")
    public ResponseEntity<Void> activateRoom(@PathVariable Integer id) {
        roomService.activateRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("@roomPermissionChecker.canDeactivate(authentication, #id)")
    public ResponseEntity<Void> deactivateRoom(@PathVariable Integer id) {
        roomService.deactivateRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
