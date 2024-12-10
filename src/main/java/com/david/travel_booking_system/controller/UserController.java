package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.UserDTO;
import com.david.travel_booking_system.dto.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.createResponse.UserCreateResponseDTO;
import com.david.travel_booking_system.dto.detail.UserDetailDTO;
import com.david.travel_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserCreateResponseDTO> createUser(@RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        UserCreateResponseDTO createdUser = UserCreateResponseDTO.from(userService.createUser(userCreateRequestDTO));
        return ResponseEntity.status(201).body(createdUser); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> users = UserDTO.from(userService.getUsers());
        return ResponseEntity.ok(users); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailDTO> getUser(@PathVariable Integer id) {
        UserDetailDTO user = UserDetailDTO.from(userService.getUserById(id));
        return ResponseEntity.ok(user); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
