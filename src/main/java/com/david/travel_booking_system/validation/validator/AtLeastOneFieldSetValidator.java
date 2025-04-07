package com.david.travel_booking_system.validation.validator;

import com.david.travel_booking_system.validation.annotation.AtLeastOneFieldSet;
import com.david.travel_booking_system.util.OptionalFieldWrapper;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class AtLeastOneFieldSetValidator implements ConstraintValidator<AtLeastOneFieldSet, Object> {

    @Override
    public boolean isValid(Object dto, ConstraintValidatorContext context) {
        if (dto == null) return false; // null DTO is invalid

        for (Field field : dto.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(dto);
                if (value instanceof OptionalFieldWrapper<?> wrapper && wrapper.isExplicitlySet()) {
                    return true; // Found at least one explicitly set field
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field in DTO validation", e);
            }
        }

        return false; // No field was explicitly set
    }
}
