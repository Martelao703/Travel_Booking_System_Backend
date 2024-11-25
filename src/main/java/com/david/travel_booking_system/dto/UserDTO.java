package com.david.travel_booking_system.dto;

import com.david.travel_booking_system.model.User;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    @NotNull(message = "ID cannot be null")
    private Integer id;

    @NotNull(message = "Active status cannot be null")
    private boolean isActive;

    @NotNull(message = "First name cannot be null")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    private String email;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phoneNumber;

    private String address;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<BookingDTO> bookings;

    public UserDTO(Integer id, boolean isActive, String firstName, String lastName, String email, String phoneNumber,
                   String address, LocalDate dateOfBirth) {
        this.id = id;
        this.isActive = isActive;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
    }

    public static UserDTO from(User user) {
        UserDTO userDTO = new UserDTO(
            user.getId(),
            user.isActive(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getAddress(),
            user.getDateOfBirth()
        );
        userDTO.setBookings(BookingDTO.from(user.getBookings()));

        return userDTO;
    }

    public static List<UserDTO> from(List<User> users) {
        return users.stream().map(UserDTO::from).collect(Collectors.toList());
    }
}
