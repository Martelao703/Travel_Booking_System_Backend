package com.david.travel_booking_system.util;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Utility class to handle generic validation and patching of entity fields from DTOs
 */
public class EntityPatcher {

    // Generically validates the entity's fields
    public static <D> void validatePatch(
            D dto,
            Set<String> criticalFields,
            Set<String> conditionallyPatchableFields,
            boolean hasBookings,
            boolean hasOngoingBookings
    ) {
        iterateFields(dto, (fieldName, fieldWrapper) -> {
            validateField(fieldName, criticalFields, conditionallyPatchableFields, hasBookings, hasOngoingBookings);
        });
    }

    // Patches the entity's fields from the DTO values
    public static <T, D> void patchEntity(T entity, D dto) {
        iterateFields(dto, (fieldName, fieldWrapper) -> {
            patchField(entity, fieldName, fieldWrapper.getValue());
        });
    }

    // Generically validates and patches the fields of an entity
    public static <T, D> void validateAndPatchEntity(
            T entity,
            D dto,
            Set<String> criticalFields,
            Set<String> conditionallyPatchableFields,
            boolean hasBookings,
            boolean hasOngoingBookings
    ) {
        iterateFields(dto, (fieldName, fieldWrapper) -> {
            validateField(fieldName, criticalFields, conditionallyPatchableFields, hasBookings, hasOngoingBookings);
            patchField(entity, fieldName, fieldWrapper.getValue());
        });
    }

    // Helper method to iterate over the DTO fields and apply the given handler logic to each explicitly set field
    private static <D> void iterateFields(D dto, FieldHandler handler) {
        for (Field dtoField : dto.getClass().getDeclaredFields()) {
            try {
                dtoField.setAccessible(true);
                OptionalFieldWrapper<?> fieldWrapper = (OptionalFieldWrapper<?>) dtoField.get(dto);
                if (!fieldWrapper.isExplicitlySet()) continue;

                handler.handle(dtoField.getName(), fieldWrapper);

            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Error accessing DTO field", e);
            }
        }
    }

    // Helper method to generically validate a single field
    private static void validateField(
            String fieldName,
            Set<String> criticalFields,
            Set<String> conditionallyPatchableFields,
            boolean hasBookings,
            boolean hasOngoingBookings
    ) {
        if (!hasBookings) return;

        if (criticalFields.contains(fieldName)) {
            throw new IllegalStateException("Cannot modify critical field '" + fieldName + "' with active bookings.");
        }

        if (hasOngoingBookings && conditionallyPatchableFields.contains(fieldName)) {
            throw new IllegalStateException("Cannot modify field '" + fieldName + "' with ongoing bookings.");
        }
    }

    // Helper method to patch a single field on the entity
    private static <T> void patchField(T entity, String fieldName, Object newValue) {
        try {
            Field entityField = entity.getClass().getDeclaredField(fieldName);
            entityField.setAccessible(true);
            Class<?> fieldType = entityField.getType();

            if (newValue != null) {
                // Handle primitive type conversion
                if (fieldType == double.class && newValue instanceof Double
                        || fieldType == int.class && newValue instanceof Integer
                        || fieldType == long.class && newValue instanceof Long
                        || fieldType == boolean.class && newValue instanceof Boolean
                        || fieldType.isAssignableFrom(newValue.getClass())) {
                    entityField.set(entity, newValue);
                } else {
                    throw new IllegalArgumentException(
                            "Type mismatch for field '" + fieldName + "'. Expected: " +
                                    fieldType.getSimpleName() + ", but got: " +
                                    newValue.getClass().getSimpleName()
                    );
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException("Error updating field dynamically", e);
        }
    }

    // Functional interface to handle DTO field operations during iteration
    @FunctionalInterface
    private interface FieldHandler {
        void handle(String fieldName, OptionalFieldWrapper<?> fieldWrapper);
    }
}
