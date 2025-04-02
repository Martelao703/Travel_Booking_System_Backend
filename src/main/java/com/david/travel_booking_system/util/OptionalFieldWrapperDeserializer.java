package com.david.travel_booking_system.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class OptionalFieldWrapperDeserializer extends JsonDeserializer<OptionalFieldWrapper<?>> {

    private final ObjectMapper objectMapper = new ObjectMapper(); // Reuse ObjectMapper for conversions

    @Override
    public OptionalFieldWrapper<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Handle explicit null (explicitly set to null)
        if (node.isNull()) {
            return new OptionalFieldWrapper<>(null);
        }

        // If it's a value node (string, number, boolean, etc.), determine the type
        if (node.isValueNode()) {
            String textValue = node.asText(); // Read as string first

            // Try parsing as LocalDateTime
            try {
                return new OptionalFieldWrapper<>(LocalDateTime.parse(textValue));
            } catch (DateTimeParseException ignored) {
                // If parsing fails, try LocalDate
            }

            // Try parsing as LocalDate
            try {
                return new OptionalFieldWrapper<>(LocalDate.parse(textValue));
            } catch (DateTimeParseException ignored) {
                // If parsing fails, fall back to generic object conversion
            }

            // Fallback to generic object conversion
            Object value = objectMapper.treeToValue(node, Object.class);
            return new OptionalFieldWrapper<>(value);
        }

        // If the payload is unexpected, throw an error
        throw new IllegalArgumentException("Invalid value for OptionalFieldWrapper: " + node.toString());
    }
}
