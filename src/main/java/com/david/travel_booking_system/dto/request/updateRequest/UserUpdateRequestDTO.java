package com.david.travel_booking_system.dto.request.updateRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequestDTO {
    @NotNull(message = "Active status cannot be null")
    private boolean isActive = true;

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
}
