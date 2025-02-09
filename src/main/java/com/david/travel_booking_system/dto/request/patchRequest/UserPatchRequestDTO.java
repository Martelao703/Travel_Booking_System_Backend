package com.david.travel_booking_system.dto.request.patchRequest;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.NotNullIfExplicitlySet;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserPatchRequestDTO {
    @NotNullIfExplicitlySet
    private OptionalFieldWrapper<Boolean> active = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private OptionalFieldWrapper<String> firstName = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private OptionalFieldWrapper<String> lastName = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @Email(message = "Invalid email format")
    private OptionalFieldWrapper<String> email = OptionalFieldWrapper.unset();

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private OptionalFieldWrapper<String> phoneNumber = OptionalFieldWrapper.unset();

    private OptionalFieldWrapper<String> address = OptionalFieldWrapper.unset();

    @Past(message = "Date of birth must be in the past")
    private OptionalFieldWrapper<LocalDate> dateOfBirth = OptionalFieldWrapper.unset();
}
