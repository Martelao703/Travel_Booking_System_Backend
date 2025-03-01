package com.david.travel_booking_system.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class OptionalFieldWrapperDeserializer extends JsonDeserializer<OptionalFieldWrapper<?>> {
    @Override
    public OptionalFieldWrapper<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Handle explicit null (explicitly set to null)
        if (node.isNull()) {
            return new OptionalFieldWrapper<>(null);
        }

        // If it's a value node (string, number, boolean, etc.), pass the value directly
        if (node.isValueNode()) {
            Object value = p.getCodec().treeToValue(node, Object.class);
            return new OptionalFieldWrapper<>(value);
        }

        // If the payload is unexpected, throw an error
        throw new IllegalArgumentException("Invalid value for OptionalFieldWrapper: " + node.toString());
    }
}
