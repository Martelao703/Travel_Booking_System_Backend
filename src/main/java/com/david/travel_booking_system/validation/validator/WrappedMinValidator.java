package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.WrappedMin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WrappedMinValidator implements ConstraintValidator<WrappedMin, OptionalFieldWrapper<Integer>> {

    private double minValue;

    @Override
    public void initialize(WrappedMin annotation) {
        this.minValue = annotation.value();
    }

    @Override
    public boolean isValid(OptionalFieldWrapper<Integer> wrapper, ConstraintValidatorContext context) {
        if (wrapper == null || !wrapper.isExplicitlySet()) return true;  // Not set → no validation needed

        Integer value = wrapper.getValue();
        if (value == null) return true; // Null is allowed

        return value >= minValue;
    }
}
