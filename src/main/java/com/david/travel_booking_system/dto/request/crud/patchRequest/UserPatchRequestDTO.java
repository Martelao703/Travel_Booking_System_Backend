package com.david.travel_booking_system.dto.request.crud.patchRequest;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.NotNullIfExplicitlySet;
import com.david.travel_booking_system.validation.annotation.WrappedEmail;
import com.david.travel_booking_system.validation.annotation.WrappedPast;
import com.david.travel_booking_system.validation.annotation.WrappedSize;
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
    @WrappedSize(max = 50, message = "First name cannot exceed 50 characters")
    private OptionalFieldWrapper<String> firstName = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @WrappedSize(max = 50, message = "Last name cannot exceed 50 characters")
    private OptionalFieldWrapper<String> lastName = OptionalFieldWrapper.unset();

    @NotNullIfExplicitlySet
    @WrappedEmail
    private OptionalFieldWrapper<String> email = OptionalFieldWrapper.unset();

    @WrappedSize(max = 15, message = "Phone number cannot exceed 15 characters")
    private OptionalFieldWrapper<String> phoneNumber = OptionalFieldWrapper.unset();

    private OptionalFieldWrapper<String> address = OptionalFieldWrapper.unset();

    @WrappedPast(message = "Date of birth must be in the past")
    private OptionalFieldWrapper<LocalDate> dateOfBirth = OptionalFieldWrapper.unset();
}
