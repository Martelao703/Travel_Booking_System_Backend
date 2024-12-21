package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.basic.UserBasicDTO;
import com.david.travel_booking_system.dto.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.full.UserFullDTO;
import com.david.travel_booking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * More info on the DTO architecture/usage on the DTO_README.md file on the dto package
 */

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<UserBasicDTO> createUser(@RequestBody UserCreateRequestDTO userCreateRequestDTO) {
        UserBasicDTO createdUser = UserBasicDTO.from(userService.createUser(userCreateRequestDTO));
        return ResponseEntity.status(201).body(createdUser); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<UserBasicDTO>> getUsers() {
        List<UserBasicDTO> users = UserBasicDTO.from(userService.getUsers());
        return ResponseEntity.ok(users); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFullDTO> getUser(@PathVariable Integer id) {
        UserFullDTO user = UserFullDTO.from(userService.getUserById(id));
        return ResponseEntity.ok(user); // Return 200 OK
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
