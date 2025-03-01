package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import com.david.travel_booking_system.validation.annotation.NotNullIfExplicitlySet;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullIfExplicitlySetValidator implements
        ConstraintValidator<NotNullIfExplicitlySet, OptionalFieldWrapper<?>> {

    @Override
    public boolean isValid(OptionalFieldWrapper<?> value, ConstraintValidatorContext context) {
        // If the field is not explicitly set, it's valid (no changes intended)
        if (!value.isExplicitlySet()) {
            return true;
        }

        Object fieldValue = value.getValue();

        // Check for null
        if (fieldValue == null) {
            return false;
        }

        // If it's a String, ensure it's not blank
        if (fieldValue instanceof String str) {
            return !str.isBlank(); // Checks both emptiness and whitespace-only strings
        }

        // For non-string fields, the non-null check above is sufficient
        return true;
    }
}
