package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.WrappedSize;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WrappedSizeValidator implements ConstraintValidator<WrappedSize, OptionalFieldWrapper<String>> {
    private int min;
    private int max;

    @Override
    public void initialize(WrappedSize annotation) {
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(OptionalFieldWrapper<String> wrapper, ConstraintValidatorContext context) {
        if (wrapper == null || !wrapper.isExplicitlySet()) return true;  // Not set → no validation needed

        String value = wrapper.getValue();
        if (value == null) return true; // Null is allowed

        return value.length() >= min && value.length() <= max;
    }
}
