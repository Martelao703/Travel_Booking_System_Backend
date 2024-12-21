package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.service.BookingService;
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

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<BookingBasicDTO> createBooking(@RequestBody BookingCreateRequestDTO bookingCreateRequestDTO) {
        BookingBasicDTO createdBooking = BookingBasicDTO.from(bookingService.createBooking(bookingCreateRequestDTO));
        return ResponseEntity.status(201).body(createdBooking); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BookingBasicDTO>> getBookings() {
        List<BookingBasicDTO> bookings = BookingBasicDTO.from(bookingService.getBookings());
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Returns BasicDTO instead of FullDTO due to the entity's absence of collection fields */
    @GetMapping("/{id}")
    public ResponseEntity<BookingBasicDTO> getBooking(@PathVariable Integer id) {
        BookingBasicDTO booking = BookingBasicDTO.from(bookingService.getBookingById(id));
        return ResponseEntity.ok(booking); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
