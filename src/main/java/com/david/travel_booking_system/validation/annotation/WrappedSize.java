package com.david.travel_booking_system.validation.annotation;

import com.david.travel_booking_system.validation.validator.WrappedSizeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = WrappedSizeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrappedSize {
    int min() default 0;
    int max() default Integer.MAX_VALUE;

    String message() default "Size out of allowed range";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
