package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.WrappedPast;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class WrappedLocalDatePastValidator implements ConstraintValidator<WrappedPast, OptionalFieldWrapper<LocalDate>> {

    @Override
    public boolean isValid(OptionalFieldWrapper<LocalDate> wrapper, ConstraintValidatorContext context) {
        if (wrapper == null || !wrapper.isExplicitlySet()) return true; // Not set → no validation needed

        LocalDate value = wrapper.getValue();
        if (value == null) return true; // Null is allowed

        return value.isBefore(LocalDate.now());
    }
}
