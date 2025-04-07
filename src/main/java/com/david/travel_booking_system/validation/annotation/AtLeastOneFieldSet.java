package com.david.travel_booking_system.validation.annotation;

import com.david.travel_booking_system.validation.validator.AtLeastOneFieldSetValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = AtLeastOneFieldSetValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AtLeastOneFieldSet {
    String message() default "At least one field must be provided.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
