package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.UserBuilder;
import com.david.travel_booking_system.dto.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public User createUser(@Valid UserCreateRequestDTO userCreateRequestDTO) {
        // Validate email uniqueness
        Optional<User> existingUserByEmail = userRepository.findByEmail(userCreateRequestDTO.getEmail());
        if (existingUserByEmail.isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        // Validate phone number uniqueness if provided
        if (userCreateRequestDTO.getPhoneNumber() != null) {
            Optional<User> existingUserByPhone = userRepository.findByPhoneNumber(userCreateRequestDTO.getPhoneNumber());
            if (existingUserByPhone.isPresent()) {
                throw new IllegalArgumentException("Phone number is already in use.");
            }
        }

        // Build User object from DTO
        User user = new UserBuilder()
                .firstName(userCreateRequestDTO.getFirstName())
                .lastName(userCreateRequestDTO.getLastName())
                .email(userCreateRequestDTO.getEmail())
                .phoneNumber(userCreateRequestDTO.getPhoneNumber())
                .address(userCreateRequestDTO.getAddress())
                .dateOfBirth(userCreateRequestDTO.getDateOfBirth())
                .build();

        // Save User
        user = userRepository.save(user);

        return user;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}
