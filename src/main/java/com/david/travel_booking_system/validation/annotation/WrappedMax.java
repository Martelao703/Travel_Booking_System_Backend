package com.david.travel_booking_system.validation.annotation;

import com.david.travel_booking_system.validation.validator.WrappedMaxValidator;
import com.david.travel_booking_system.validation.validator.WrappedDoubleMaxValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {WrappedMaxValidator.class, WrappedDoubleMaxValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrappedMax {
    double value();

    String message() default "Value exceeds maximum allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
