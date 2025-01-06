package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.request.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, BookingService bookingService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.bookingService = bookingService;
        this.userMapper = userMapper;
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

        // Create User from DTO
        User user = userMapper.createUserFromDTO(userCreateRequestDTO);

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

        // Update User from DTO
        userMapper.updateUserFromDTO(user, userUpdateRequestDTO);

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
