package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.request.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.dto.request.patchRequest.BookingPatchRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.BookingUpdateRequestDTO;
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
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;
    private final BookingMapper bookingMapper;

    @Autowired
    public BookingController(BookingService bookingService, BookingMapper bookingMapper) {
        this.bookingService = bookingService;
        this.bookingMapper = bookingMapper;
    }

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
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(bookingService.getBookings());
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Returns BasicDTO instead of FullDTO due to the entity's absence of collection fields */
    @GetMapping("/{id}")
    public ResponseEntity<BookingBasicDTO> getBooking(@PathVariable Integer id) {
        BookingBasicDTO booking = bookingMapper.toBasicDTO(bookingService.getBookingById(id));
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
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
