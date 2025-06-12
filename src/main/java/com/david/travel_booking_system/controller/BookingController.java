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
import org.springframework.security.access.prepost.PreAuthorize;
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

    /* CRUD and Basic Endpoints ------------------------------------------------------------------------------------ */

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    @PreAuthorize("@bookingPermissionChecker.canCreate(authentication, #createDTO.userId)")
    public ResponseEntity<BookingBasicDTO> createBooking(
            @RequestBody @Valid BookingCreateRequestDTO createDTO
    ) {
        BookingBasicDTO createdBooking = bookingMapper.toBasicDTO(bookingService.createBooking(createDTO));
        return ResponseEntity.status(201).body(createdBooking); // Return 201 Created
    }

    @GetMapping
    @PreAuthorize("@bookingPermissionChecker.canReadAny(authentication)")
    public ResponseEntity<List<BookingBasicDTO>> getBookings() {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(bookingService.getBookings(false));
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Returns BasicDTO instead of FullDTO due to the entity's absence of collection fields */
    @GetMapping("/{id}")
    @PreAuthorize("@bookingPermissionChecker.canRead(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> getBooking(@PathVariable Integer id) {
        BookingBasicDTO booking = bookingMapper.toBasicDTO(bookingService.getBookingById(id, false));
        return ResponseEntity.ok(booking); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    @PreAuthorize("@bookingPermissionChecker.canUpdate(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> updateBooking(
            @PathVariable Integer id,
            @RequestBody @Valid BookingUpdateRequestDTO updateDTO
    ) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.updateBooking(id, updateDTO));
        return ResponseEntity.ok(updatedBooking); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PatchMapping("/{id}")
    @PreAuthorize("@bookingPermissionChecker.canUpdate(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> patchBooking(
            @PathVariable Integer id,
            @RequestBody @Valid BookingPatchRequestDTO patchDTO
    ) {
        BookingBasicDTO patchedBooking = bookingMapper.toBasicDTO(bookingService.patchBooking(id, patchDTO));
        return ResponseEntity.ok(patchedBooking); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@bookingPermissionChecker.canDelete(authentication, #id)")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        bookingService.softDeleteBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("@bookingPermissionChecker.canDelete(authentication, #id)")
    public ResponseEntity<Void> hardDeleteBooking(@PathVariable Integer id) {
        bookingService.hardDeleteBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("@bookingPermissionChecker.canRestore(authentication, #id)")
    public ResponseEntity<Void> restoreBooking(@PathVariable Integer id) {
        bookingService.restoreBooking(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Custom Endpoints -------------------------------------------------------------------------------------------- */

    // Change check-in and/or check-out dates of a booking
    @PatchMapping("/{id}/dates")
    @PreAuthorize("@bookingPermissionChecker.canChangeDates(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> changeBookingDates(
            @PathVariable Integer id,
            @RequestBody @Valid BookingDateChangeRequestDTO dateChangeDTO
    ) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(
                bookingService.changeBookingDates(id, dateChangeDTO)
        );
        return ResponseEntity.ok(updatedBooking);
    }

    @PatchMapping("/{id}/confirm-payment")
    @PreAuthorize("@bookingPermissionChecker.canConfirmPayment(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> confirmPayment(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.confirmPayment(id));
        return ResponseEntity.ok(updatedBooking);
    }

    @PatchMapping("/{id}/check-in")
    @PreAuthorize("@bookingPermissionChecker.canCheckIn(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> checkIn(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.checkIn(id));
        return ResponseEntity.ok(updatedBooking);
    }

    @PatchMapping("/{id}/check-out")
    @PreAuthorize("@bookingPermissionChecker.canCheckOut(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> checkOut(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.checkOut(id));
        return ResponseEntity.ok(updatedBooking);
    }

    // User cancels a booking
    @PatchMapping("/{id}/cancel")
    @PreAuthorize("@bookingPermissionChecker.canCancel(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> cancelBooking(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.cancelBooking(id));
        return ResponseEntity.ok(updatedBooking);
    }

    // Property/Admin rejects a booking
    @PatchMapping("/{id}/reject")
    @PreAuthorize("@bookingPermissionChecker.canReject(authentication, #id)")
    public ResponseEntity<BookingBasicDTO> rejectBooking(@PathVariable Integer id) {
        BookingBasicDTO updatedBooking = bookingMapper.toBasicDTO(bookingService.rejectBooking(id));
        return ResponseEntity.ok(updatedBooking);
    }
}
