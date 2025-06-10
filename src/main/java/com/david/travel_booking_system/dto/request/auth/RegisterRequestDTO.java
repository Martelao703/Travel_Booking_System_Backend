package com.david.travel_booking_system.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class RegisterRequestDTO {
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phoneNumber;

    private String address;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}
