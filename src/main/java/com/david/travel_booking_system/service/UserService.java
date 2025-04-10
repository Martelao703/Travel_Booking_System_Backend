package com.david.travel_booking_system.service;

import com.david.travel_booking_system.dto.request.auth.RegisterRequestDTO;
import com.david.travel_booking_system.dto.request.crud.createRequest.UserCreateRequestDTO;
import com.david.travel_booking_system.dto.request.crud.patchRequest.UserPatchRequestDTO;
import com.david.travel_booking_system.dto.request.crud.updateRequest.UserUpdateRequestDTO;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.enumsAndSets.UserRole;
import com.david.travel_booking_system.enumsAndSets.entityPatchRequestFieldRules.UserPatchFieldRules;
import com.david.travel_booking_system.mapper.UserMapper;
import com.david.travel_booking_system.model.Booking;
import com.david.travel_booking_system.model.User;
import com.david.travel_booking_system.repository.BookingRepository;
import com.david.travel_booking_system.repository.UserRepository;
import com.david.travel_booking_system.specification.BaseSpecifications;
import com.david.travel_booking_system.specification.BookingSpecifications;
import com.david.travel_booking_system.specification.UserSpecifications;
import com.david.travel_booking_system.util.EntityPatcher;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.david.travel_booking_system.enumsAndSets.UserRole.ROLE_USER;
import static java.util.Collections.singleton;

@Service
public class UserService {
    // Repositories
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    // Mappers
    private final UserMapper userMapper;

    // Other
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BookingRepository bookingRepository, UserMapper userMapper,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /* CRUD and Basic Methods -------------------------------------------------------------------------------------- */

    @Transactional
    public User createUser(RegisterRequestDTO registerRequestDTO) {
        // Validate email and phone number uniqueness
        String email = registerRequestDTO.getEmail();
        String phoneNumber = registerRequestDTO.getPhoneNumber();

        if (existsByEmail(email)) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (phoneNumber != null && existsByPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }

        // Create User from DTO
        User user = userMapper.createUserFromDTO(registerRequestDTO);

        // Encode the password
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        // Set default role
        Set<UserRole> roles = new HashSet<>();
        roles.add(ROLE_USER);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getUsers(boolean includeDeleted) {
        Specification<User> spec = includeDeleted
                ? Specification.where(null)  // No spec
                : BaseSpecifications.excludeDeleted(User.class); // Non-deleted filter

        return userRepository.findAll(spec);
    }

    @Transactional(readOnly = true)
    public User getUserById(Integer id, boolean includeDeleted) {
        Specification<User> spec = includeDeleted
                ? BaseSpecifications.filterById(User.class, id) // ID filter
                : BaseSpecifications.filterById(User.class, id)
                .and(BaseSpecifications.excludeDeleted(User.class)); // ID and non-deleted filter

        return userRepository.findOne(spec)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " not found"));
    }

    @Transactional
    public User updateUser(Integer id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User user = getUserById(id, false);

        // Check if user has any bookings in progress
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot update user with bookings in progress");
        }

        // Validate email and phone number uniqueness
        String newEmail = userUpdateRequestDTO.getEmail();
        String newPhoneNumber = userUpdateRequestDTO.getPhoneNumber();

        if (!newEmail.equals(user.getEmail()) && existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (newPhoneNumber != null && !newPhoneNumber.equals(user.getPhoneNumber())
                && existsByPhoneNumber(newPhoneNumber)) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }

        // Update User from DTO
        userMapper.updateUserFromDTO(user, userUpdateRequestDTO);

        return userRepository.save(user);
    }

    @Transactional
    public User patchUser(Integer id, UserPatchRequestDTO userPatchRequestDTO) {
        User user = getUserById(id, false);

        // Booking conditions
        boolean hasBookings = false;
        boolean hasOngoingBookings = false;
        boolean hasAnyFieldRules = !UserPatchFieldRules.CRITICAL_FIELDS.isEmpty()
                || !UserPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty();

        // Query for bookings only if necessary
        if (hasAnyFieldRules) {
            hasBookings = hasActiveBookings(id);
            if (hasBookings && !UserPatchFieldRules.CONDITIONALLY_PATCHABLE_FIELDS.isEmpty()) {
                hasOngoingBookings = hasOngoingBookings(id);
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

        if (newEmail != null && existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email is already in use.");
        }
        if (newPhoneNumber != null && existsByPhoneNumber(newPhoneNumber)) {
            throw new IllegalArgumentException("Phone number is already in use.");
        }

        // Patch User
        EntityPatcher.patchEntity(user, userPatchRequestDTO);

        return userRepository.save(user);
    }

    @Transactional
    public void softDeleteUser(Integer id) {
        User user = getUserById(id, false);

        // Check if user has any active bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot delete user with active bookings");
        }

        // Soft delete user
        user.setActive(false);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Transactional
    public void hardDeleteUser(Integer id) {
        User user = getUserById(id, true);

        // Check if user is not soft-deleted
        if (!user.isDeleted()) {
            // Check if user has any active bookings
            if (hasActiveBookings(id)) {
                throw new IllegalStateException("Cannot delete user with active bookings");
            }
        }

        // Hard delete user
        userRepository.delete(user);
    }

    @Transactional
    public void restoreUser(Integer id) {
        User user = getUserById(id, true);

        // Check if user is soft-deleted
        if (!user.isDeleted()) {
            throw new IllegalStateException("User is not soft-deleted");
        }

        // Restore user
        user.setDeleted(false);
        userRepository.save(user);
    }

    /* Custom methods ---------------------------------------------------------------------------------------------- */

    @Transactional
    public void activateUser(Integer id) {
        User user = getUserById(id, true);

        // Check if user is not already active
        if (user.isActive()) {
            throw new IllegalStateException("User is already active");
        }

        // Activate user
        user.setActive(true);
        userRepository.save(user);
    }

    @Transactional
    public void deactivateUser(Integer id) {
        User user = getUserById(id, true);

        // Check if user is not already inactive
        if (!user.isActive()) {
            throw new IllegalStateException("User is already inactive");
        }

        // Check if user has any active bookings
        if (hasActiveBookings(id)) {
            throw new IllegalStateException("Cannot deactivate user with active bookings");
        }

        // Deactivate user
        user.setActive(false);
        userRepository.save(user);
    }

    /* Helper methods ---------------------------------------------------------------------------------------------- */

    private boolean hasActiveBookings(Integer id) {
        // Filter bookings by user ID and relevant statuses
        Specification<Booking> bookingSpec = BookingSpecifications.filterByUserId(id)
                .and(BookingSpecifications.filterByStatuses(List.of(
                        BookingStatus.PENDING, BookingStatus.CONFIRMED, BookingStatus.ONGOING
                )));
        return bookingRepository.exists(bookingSpec);
    }

    private boolean hasOngoingBookings(Integer id) {
        // Filter bookings by user ID and ONGOING status
        Specification<Booking> bookingSpec = BookingSpecifications.filterByUserId(id)
                .and(BookingSpecifications.filterByStatus(BookingStatus.ONGOING));
        return bookingRepository.exists(bookingSpec);
    }

    private boolean existsByEmail(String email) {
        // Filter users by email
        Specification<User> userSpec = UserSpecifications.filterByEmail(email);
        return userRepository.exists(userSpec);
    }

    private boolean existsByPhoneNumber(String phoneNumber) {
        // Filter users by phone number
        Specification<User> userSpec = UserSpecifications.filterByPhoneNumber(phoneNumber);
        return userRepository.exists(userSpec);
    }
}
