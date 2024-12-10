package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.BookingDTO;
import com.david.travel_booking_system.dto.createRequest.BookingCreateRequestDTO;
import com.david.travel_booking_system.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingCreateRequestDTO bookingCreateRequestDTO) {
        BookingDTO createdBooking = BookingDTO.from(bookingService.createBooking(bookingCreateRequestDTO));
        return ResponseEntity.status(201).body(createdBooking); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getBookings() {
        List<BookingDTO> bookings = BookingDTO.from(bookingService.getBookings());
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Integer id) {
        BookingDTO booking = BookingDTO.from(bookingService.getBookingById(id));
        return ResponseEntity.ok(booking); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
