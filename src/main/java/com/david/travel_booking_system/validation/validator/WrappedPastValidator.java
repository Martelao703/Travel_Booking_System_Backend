package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.WrappedPast;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class WrappedPastValidator implements ConstraintValidator<WrappedPast, OptionalFieldWrapper<LocalDateTime>> {

    @Override
    public boolean isValid(OptionalFieldWrapper<LocalDateTime> wrapper, ConstraintValidatorContext context) {
        if (wrapper == null || !wrapper.isExplicitlySet()) return true; // Not set → no validation needed

        LocalDateTime value = wrapper.getValue();
        if (value == null) return true; // Null is allowed

        return value.isBefore(LocalDateTime.now());
    }
}
