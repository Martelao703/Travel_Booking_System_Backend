package com.david.travel_booking_system.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class OptionalFieldWrapperDeserializer extends JsonDeserializer<OptionalFieldWrapper<?>>
        implements ContextualDeserializer {

    private JavaType valueType; // The expected inner type (T in OptionalFieldWrapper<T>)
    private final ObjectMapper strictMapper;

    // Default constructor for Jackson
    public OptionalFieldWrapperDeserializer() {
        // Create an ObjectMapper instance dedicated to this deserializer.
        // Configure it to be strict about type conversion.
        strictMapper = new ObjectMapper();
        strictMapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, false);

        // Register the JavaTimeModule to handle LocalDateTime
        strictMapper.registerModule(new JavaTimeModule());
        strictMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // Private constructor used by createContextual
    private OptionalFieldWrapperDeserializer(JavaType valueType, ObjectMapper strictMapper) {
        this.valueType = valueType;
        this.strictMapper = strictMapper;
    }

    // Get the expected type of the inner value (T in OptionalFieldWrapper<T>)
    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
            throws JsonMappingException {
        if (property != null) {
            // Get the type of OptionalFieldWrapper<T>
            JavaType wrapperType = property.getType();

            // Extract T from OptionalFieldWrapper<T>
            JavaType innerType = wrapperType.containedType(0);

            return new OptionalFieldWrapperDeserializer(innerType, strictMapper);
        }
        return this;
    }

    @Override
    public OptionalFieldWrapper<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Handle explicit null (explicitly set to null)
        if (node.isNull() || (node.getNodeType() == JsonNodeType.NULL)) {
            return new OptionalFieldWrapper<>(null);
        }

        // Use the strictly configured mapper to convert the node to the expected type
        // If the type in JSON doesn’t match exactly, this will throw an exception
        Object value = strictMapper.readValue(p.getCodec().treeAsTokens(node), valueType);

        return new OptionalFieldWrapper<>(value);
    }
}

