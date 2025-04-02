package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.request.specialized.BookingDateChangeRequestDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.BookingPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.BookingUpdateRequestDTO;
import com.david.travel_booking_system.mapper.BookingMapper;
import com.david.travel_booking_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingController(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<BookingBasicDTO> createBooking(
            @RequestBody @Valid BookingCreateRequestDTO bookingCreateRequestDTO
    ) {
        BookingBasicDTO createdBooking = bookingMapper.toBasicDTO(bookingService.createBooking(bookingCreateRequestDTO));
        return ResponseEntity.status(201).body(createdBooking); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BookingBasicDTO>> getBookings() {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(bookingService.getBookings(false));
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Returns BasicDTO instead of FullDTO due to the entity's absence of collection fields */
    @GetMapping("/{id}")
    public ResponseEntity<BookingBasicDTO> getBooking(@PathVariable Integer id) {
        BookingBasicDTO booking = bookingMapper.toBasicDTO(bookingService.getBookingById(id, false));
        return ResponseEntity.ok(booking); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    public ResponseEntity<BookingBasicDTO> updateBooking(
            @PathVariable Integer id,
            @RequestBody @Valid BookingUpdateRequestDTO bookingUpdateRequestDTO
    ) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.updateBooking(id, bookingUpdateRequestDTO));
        return ResponseEntity.ok(updatedBooking); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PatchMapping("/{id}")
    public ResponseEntity<BookingBasicDTO> patchBooking(
            @PathVariable Integer id,
            @RequestBody @Valid BookingPatchRequestDTO bookingPatchRequestDTO
    ) {
        BookingBasicDTO patchedBooking = bookingMapper.toBasicDTO(bookingService.patchBooking(id, bookingPatchRequestDTO));
        return ResponseEntity.ok(patchedBooking); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        bookingService.softDeleteBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteBooking(@PathVariable Integer id) {
        bookingService.hardDeleteBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */

    // Get all bookings of a room
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<BookingBasicDTO>> getBookingsByRoomId(@PathVariable Integer roomId) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByRoomId(roomId, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    // Get all bookings of a property
    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<BookingBasicDTO>> getBookingsByPropertyId(@PathVariable Integer propertyId) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByPropertyId(propertyId, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    // Get all bookings of a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingBasicDTO>> getBookingsByUserId(@PathVariable Integer userId) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByUserId(userId, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    // Change check-in and/or check-out dates of a booking
    @PatchMapping("/{id}/dates")
    public ResponseEntity<BookingBasicDTO> changeBookingDates(
            @PathVariable Integer id,
            @RequestBody @Valid BookingDateChangeRequestDTO bookingDateChangeRequestDTO
    ) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(
                bookingService.changeBookingDates(id, bookingDateChangeRequestDTO)
        );
        return ResponseEntity.ok(updatedBooking);
    }

    @PatchMapping("/{id}/confirm-payment")
    public ResponseEntity<BookingBasicDTO> confirmPayment(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.confirmPayment(id));
        return ResponseEntity.ok(updatedBooking);
    }

    @PatchMapping("/{id}/check-in")
    public ResponseEntity<BookingBasicDTO> checkIn(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.checkIn(id));
        return ResponseEntity.ok(updatedBooking);
    }

    @PatchMapping("/{id}/check-out")
    public ResponseEntity<BookingBasicDTO> checkOut(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.checkOut(id));
        return ResponseEntity.ok(updatedBooking);
    }

    // User cancels a booking
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingBasicDTO> cancelBooking(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.cancelBooking(id));
        return ResponseEntity.ok(updatedBooking);
    }

    // Property/Admin rejects a booking
    @PatchMapping("/{id}/reject")
    public ResponseEntity<BookingBasicDTO> rejectBooking(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.rejectBooking(id));
        return ResponseEntity.ok(updatedBooking);
    }
}
