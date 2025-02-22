package com.david.travel_booking_system.controller;

import com.david.travel_booking_system.dto.response.basic.UserBasicDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.response.full.UserFullDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.UserPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.mapper.UserMapper;
import com.david.travel_booking_system.service.UserService;
import jakarta.validation.Valid;
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
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    /* Returns BasicDTO instead of DetailDTO due to the entity's absence of non-nested collection fields */
    @PostMapping
    public ResponseEntity<UserBasicDTO> createUser(@RequestBody @Valid UserCreateRequestDTO userCreateRequestDTO) {
        UserBasicDTO createdUser = userMapper.toBasicDTO(userService.createUser(userCreateRequestDTO));
        return ResponseEntity.status(201).body(createdUser); // Return 201 Created
    }

    @GetMapping
    public ResponseEntity<List<UserBasicDTO>> getUsers() {
        List<UserBasicDTO> users = userMapper.toBasicDTOs(userService.getUsers());
        return ResponseEntity.ok(users); // Return 200 OK
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserFullDTO> getUser(@PathVariable Integer id) {
        UserFullDTO user = userMapper.toFullDTO(userService.getUserById(id));
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
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
