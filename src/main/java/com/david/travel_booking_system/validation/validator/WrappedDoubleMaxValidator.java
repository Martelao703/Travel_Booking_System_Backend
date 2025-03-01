package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.WrappedMax;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WrappedDoubleMaxValidator implements ConstraintValidator<WrappedMax, OptionalFieldWrapper<Double>> {

    private double maxValue;

    @Override
    public void initialize(WrappedMax annotation) {
        this.maxValue = annotation.value();
    }

    @Override
    public boolean isValid(OptionalFieldWrapper<Double> wrapper, ConstraintValidatorContext context) {
        if (wrapper == null || !wrapper.isExplicitlySet()) return true;  // Not set → no validation needed

        Double value = wrapper.getValue();
        if (value == null) return true; // Null is allowed

        return value <= maxValue;
    }
}
