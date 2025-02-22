package com.david.travel_booking_system.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * Wrapper class for optional fields in request DTOs to differentiate between explicitly set and unset fields in the
 * incoming payload
 * @param <T> Type of the field value
 */
@Getter
public class OptionalFieldWrapper<T> {
    private final T value;
    private final boolean explicitlySet;

    @JsonCreator // Used by Jackson to create instances of this class from JSON
    public OptionalFieldWrapper(T value) {
        this.value = value;
        this.explicitlySet = true; // Field is explicitly set if it's included in the payload
    }

    private OptionalFieldWrapper() {
        this.value = null;
        this.explicitlySet = false; // Field is not explicitly set if omitted from the payload
    }

    // Factory method used to initialize the field in an unset state
    public static <T> OptionalFieldWrapper<T> unset() {
        return new OptionalFieldWrapper<>();
    }

    // Execute a lambda if explicitly set
    public void ifExplicitlySet(Consumer<T> action) {
        if (explicitlySet) {
            action.accept(value);
        }
    }
}
