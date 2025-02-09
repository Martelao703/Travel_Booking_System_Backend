package com.david.travel_booking_system.validation;

import com.david.travel_booking_system.util.OptionalFieldWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotNullIfExplicitlySetValidator implements
        ConstraintValidator<NotNullIfExplicitlySet, OptionalFieldWrapper<?>> {

    @Override
    public boolean isValid(OptionalFieldWrapper<?> value, ConstraintValidatorContext context) {
        // If the field is explicitly set, value must not be null
        return !value.isExplicitlySet() || value.getValue() != null;
    }
}
