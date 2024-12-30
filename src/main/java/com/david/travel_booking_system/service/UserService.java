package com.david.travel_booking_system.service;

import com.david.travel_booking_system.builder.UserBuilder;
import com.david.travel_booking_system.dto.request.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BookingService bookingService;

    @Autowired
    public UserService(UserRepository userRepository, BookingService bookingService) {
        this.userRepository = userRepository;
        this.bookingService = bookingService;
    }

    /* Basic CRUD -------------------------------------------------------------------------------------------------- */

    @Transactional
    public User createUser(UserCreateRequestDTO userCreateRequestDTO) {
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
        return userRepository.save(user);
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

    @Transactional
    public User updateUser(Integer id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = getUserById(id);

        // Update User object from DTO
        user.setActive(userUpdateRequestDTO.isActive());
        user.setFirstName(userUpdateRequestDTO.getFirstName());
        user.setLastName(userUpdateRequestDTO.getLastName());
        user.setEmail(userUpdateRequestDTO.getEmail());
        user.setPhoneNumber(userUpdateRequestDTO.getPhoneNumber());
        user.setAddress(userUpdateRequestDTO.getAddress());
        user.setDateOfBirth(userUpdateRequestDTO.getDateOfBirth());

        // Save User
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer userId) {
        User user = getUserById(userId);

        // Check if user has any bookings in progress
        boolean hasBookings = bookingService.existsBookingsForUser(user.getId());
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete user with bookings in progress");
        }

        // Delete user
        userRepository.deleteById(userId);
    }

    /* Add to / Remove from lists ---------------------------------------------------------------------------------- */
}
