package com.david.travel_booking_system.validation.annotation;

import com.david.travel_booking_system.validation.validator.NotNullIfExplicitlySetValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullIfExplicitlySetValidator.class)
public @interface NotNullIfExplicitlySet {
    String message() default "Field cannot be null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
