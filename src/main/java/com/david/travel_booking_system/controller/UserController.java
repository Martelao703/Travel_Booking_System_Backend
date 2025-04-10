package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.BookingBasicDTO;
import com.david.travel_booking_system.dto.response.basic.UserBasicDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.response.full.UserFullDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.UserPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.mapper.BookingMapper;
import com.david.travel_booking_system.mapper.UserMapper;
import com.david.travel_booking_system.service.BookingService;
import com.david.travel_booking_system.service.UserService;
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
@RequestMapping("/api/users")
public class UserController {
    // Services
    private final UserService userService;
    private final BookingService bookingService;

    // Mappers
    private final UserMapper userMapper;
    private final BookingMapper bookingMapper;

    @Autowired
    public UserController(UserService userService, BookingService bookingService, UserMapper userMapper,
                          BookingMapper bookingMapper) {
        this.userService = userService;
        this.bookingService = bookingService;
        this.userMapper = userMapper;
        this.bookingMapper = bookingMapper;
    }

    /* CRUD and Basic Endpoints ------------------------------------------------------------------------------------ */

    /* Create User endpoint delegated to AuthController endpoint 'register' */

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity<List<UserBasicDTO>> getUsers() {
        List<UserBasicDTO> users = userMapper.toBasicDTOs(userService.getUsers(false));
        return ResponseEntity.ok(users); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFullDTO> getUser(@PathVariable Integer id) {
        UserFullDTO user = userMapper.toFullDTO(userService.getUserById(id, false));
        return ResponseEntity.ok(user); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PutMapping("/{id}")
    public ResponseEntity<UserBasicDTO> updateUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        UserBasicDTO updatedUser = userMapper.toBasicDTO(userService.updateUser(id, userUpdateRequestDTO));
        return ResponseEntity.ok(updatedUser); // Return 200 OK
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PatchMapping("/{id}")
    public ResponseEntity<UserBasicDTO> patchUser(
            @PathVariable Integer id,
            @RequestBody @Valid UserPatchRequestDTO userPatchRequestDTO
    ) {
        UserBasicDTO patchedUser = userMapper.toBasicDTO(userService.patchUser(id, userPatchRequestDTO));
        return ResponseEntity.ok(patchedUser); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.softDeleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteUser(@PathVariable Integer id) {
        userService.hardDeleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Void> restoreUser(@PathVariable Integer id) {
        userService.restoreUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    /* Get Lists of Nested Entities */

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<BookingBasicDTO>> getBookings(@PathVariable Integer id) {
        List<BookingBasicDTO> bookings = bookingMapper.toBasicDTOs(
                bookingService.getBookingsByUserId(id, false)
        );
        return ResponseEntity.ok(bookings); // Return 200 OK
    }

    /* Custom endpoints -------------------------------------------------------------------------------------------- */

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateUser(@PathVariable Integer id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable Integer id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
