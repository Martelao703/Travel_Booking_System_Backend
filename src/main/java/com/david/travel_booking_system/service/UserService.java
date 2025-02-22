package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.crud.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.UserPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.UserPatchFieldRules;
import com.david.travel_booking_system.mapper.UserMapper;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.UserRepository;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        // Validate email and phone number uniqueness
        String email = userCreateRequestDTO.getEmail();
        String phoneNumber = userCreateRequestDTO.getPhoneNumber();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (phoneNumber != null && userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }

        // Create User from DTO
        User user = userMapper.createUserFromDTO(userCreateRequestDTO);

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

        // Check if user has any bookings in progress
        boolean hasBookings = bookingService.existsBookingsForUser(user.getId());
        if (hasBookings) {
            throw new IllegalStateException("Cannot update user with bookings in progress");
        }

        // Validate email and phone number uniqueness
        String newEmail = userUpdateRequestDTO.getEmail();
        String newPhoneNumber = userUpdateRequestDTO.getPhoneNumber();

        if (!newEmail.equals(user.getEmail()) && userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (newPhoneNumber != null && !newPhoneNumber.equals(user.getPhoneNumber())
                && userRepository.findByPhoneNumber(newPhoneNumber).isPresent()) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }

        // Update User from DTO
        userMapper.updateUserFromDTO(user, userUpdateRequestDTO);

        return userRepository.save(user);
    }

    @Transactional
    public User patchUser(Integer id, UserPatchRequestDTO userPatchRequestDTO) {
        User user = getUserById(id);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !UserPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !UserPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = bookingService.existsBookingsForUser(id);
            if (hasBookings && !UserPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = bookingService.existsOngoingBookingsForUser(id);
            }
        }

        // Validate user
        EntityPatcher.validatePatch(
                userPatchRequestDTO,
                UserPatchFieldRules.CRITICAL_FIELDS,
                UserPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS,
                hasBookings,
                hasOngoingBookings
        );

        // Validate email and phone number uniqueness if provided
        String newEmail = userPatchRequestDTO.getEmail().getValue();
        String newPhoneNumber = userPatchRequestDTO.getPhoneNumber().getValue();

        if (newEmail != null && userRepository.findByEmail(newEmail).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (newPhoneNumber != null && userRepository.findByPhoneNumber(newPhoneNumber).isPresent()) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }

        // Patch User
        EntityPatcher.patchEntity(user, userPatchRequestDTO);

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

        userRepository.deleteById(userId);
    }

    /* Helper methods ---------------------------------------------------------------------------------------------- */
}
