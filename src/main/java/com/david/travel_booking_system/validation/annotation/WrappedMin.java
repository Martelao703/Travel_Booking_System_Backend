package com.david.travel_booking_system.validation.annotation;

import com.david.travel_booking_system.validation.validator.WrappedDoubleMinValidator;
import com.david.travel_booking_system.validation.validator.WrappedMinValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {WrappedMinValidator.class, WrappedDoubleMinValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrappedMin {
    double value();

    String message() default "Value is below minimum allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
