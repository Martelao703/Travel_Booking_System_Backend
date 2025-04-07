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
    public ResponseEntity<RoomBasicDTO> createRoom(@RequestBody @Valid RoomCreateRequestDTO roomCreateRequestDTO) {
        RoomBasicDTO createdRoom = roomMapper.toBasicDTO(roomService.createRoom(roomCreateRequestDTO));
        return ResponseEntity.status(201).body(createdRoom); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<RoomBasicDTO>> getRooms() {
        List<RoomBasicDTO> rooms = roomMapper.toBasicDTOs(roomService.getRooms(false));
        return ResponseEntity.ok(rooms); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomFullDTO> getRoom(@PathVariable Integer id) {
        RoomFullDTO room = roomMapper.toFullDTO(roomService.getRoomById(id, false));
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
        roomService.softDeleteRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteRoom(@PathVariable Integer id) {
        roomService.hardDeleteRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreRoom(@PathVariable Integer id) {
        roomService.restoreRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Get Lists of Nested Entities */

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingBasicDTO>> getBookings(@PathVariable Integer id) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByRoomId(id, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateRoom(@PathVariable Integer id) {
        roomService.activateRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateRoom(@PathVariable Integer id) {
        roomService.deactivateRoom(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
