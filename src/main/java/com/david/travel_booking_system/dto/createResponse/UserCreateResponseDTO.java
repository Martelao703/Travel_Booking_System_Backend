package com.david.travel_booking_system.dto.createResponse;

import com.david.travel_booking_system.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserCreateResponseDTO {
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

    public static UserCreateResponseDTO from(User user) {
        return new UserCreateResponseDTO(
                user.getId(),
                user.isActive(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getDateOfBirth()
        );
    }

    public static List<UserCreateResponseDTO> from(List<User> users) {
        return users.stream().map(UserCreateResponseDTO::from).collect(Collectors.toList());
    }
}
