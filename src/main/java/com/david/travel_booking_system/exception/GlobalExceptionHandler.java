package com.david.travel_booking_system.exception;

import com.david.travel_booking_system.enumsAndSets.BedType;
import com.david.travel_booking_system.enumsAndSets.BookingStatus;
import com.david.travel_booking_system.enumsAndSets.PropertyType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle ConstraintViolationException in Entities
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

        log.warn("Constraint violation: {}", errors);

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errors,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle MethodArgumentNotValidException in DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        // Add field-level validation errors
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        // Add global (class-level) validation errors
        ex.getBindingResult().getGlobalErrors().forEach(error ->
                errors.put(error.getObjectName(), error.getDefaultMessage())
        );

        log.warn("DTO validation failed: {}", errors);

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errors,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle EntityNotFound
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        log.warn("Entity not found: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // Handle IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.warn("Illegal argument: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle IllegalStateException
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiError> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        log.warn("Illegal state: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    // Handle DataAccessException
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiError> handleDataAccessException(DataAccessException ex, WebRequest request) {
        log.error("Database access error", ex);

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Database error: unable to access data at this time",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle HttpMessageNotReadableException
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        String message = "Malformed JSON request"; // default fallback
        Throwable cause = ex.getCause();

        // Case 1: InvalidFormatException - Type mismatch
        if (cause instanceof InvalidFormatException invalidFormatEx) {
            String fieldPath = "unknown";
            if (!invalidFormatEx.getPath().isEmpty()) {
                fieldPath = invalidFormatEx.getPath().getFirst().getFieldName();
            }

            // Extract the expected and received types from the exception
            String expectedType = invalidFormatEx.getTargetType().getSimpleName();
            String receivedType = invalidFormatEx.getValue() != null
                    ? invalidFormatEx.getValue().getClass().getSimpleName()
                    : "null";

            message = String.format("Invalid type for field '%s'. Expected: %s, but received: %s",
                    fieldPath, expectedType, receivedType);

            log.warn("InvalidFormatException on field '{}': expected {}, but received {}",
                    fieldPath, expectedType, receivedType);
        }

        // Case 2: JsonMappingException - Enum mismatch
        else if (cause instanceof JsonMappingException mappingEx) {
            String fieldPath = !mappingEx.getPath().isEmpty()
                    ? mappingEx.getPath().getFirst().getFieldName()
                    : "unknown";

            // Extract the enum class and the provided value from the error message
            String rawMsg = mappingEx.getOriginalMessage();
            String enumTypeName = "Enum";
            String invalidValue = "invalid";

            if (rawMsg.contains(":")) {
                String[] parts = rawMsg.split(":");
                enumTypeName = parts[1].replace("Invalid ", "").trim();
                invalidValue = parts[2].trim();
            }

            // Generate allowed values based on the enum type
            String allowedValues = switch (enumTypeName) {
                case "BedType" -> String.join(", ", Arrays.stream(BedType.values()).map(Enum::name).toList());
                case "PropertyType" -> String.join(", ", Arrays.stream(PropertyType.values()).map(Enum::name).toList());
                case "BookingStatus" -> String.join(", ", Arrays.stream(BookingStatus.values()).map(Enum::name).toList());
                default -> "unknown";
            };

            message = String.format("Invalid value for field '%s'. Invalid %s: %s. Allowed values: [%s]",
                    fieldPath, enumTypeName, invalidValue, allowedValues);

            log.warn("Enum mapping error for field '{}': {} not in [{}]", fieldPath, invalidValue, allowedValues);
        }

        else {
            log.warn("Unreadable HTTP message: {}", ex.getMessage());
        }

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                message,
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException ex, WebRequest request) {
        log.warn("General validation error: {}", ex.getMessage());

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error: " + ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex, WebRequest request) {
        log.error("Unhandled exception occurred", ex);

        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
