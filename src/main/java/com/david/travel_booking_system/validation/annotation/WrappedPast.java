package com.david.travel_booking_system.validation.annotation;

import com.david.travel_booking_system.validation.validator.WrappedLocalDatePastValidator;
import com.david.travel_booking_system.validation.validator.WrappedPastValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {WrappedPastValidator.class, WrappedLocalDatePastValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrappedPast {
    String message() default "Date must be in the past";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
