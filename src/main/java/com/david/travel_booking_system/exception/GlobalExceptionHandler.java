package com.david.travel_booking_system.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle ConstraintViolationException in Entities
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation ->
                errors.put(violation.getPropertyPath().toString(), violation.getMessage())
        );

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
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                "Validation Error",
                errors,
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // 3. Handle EntityNotFound
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    // Handle general exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An unexpected error occurred. Please try again.",
                request.getDescription(false).replace("uri=", "")
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}