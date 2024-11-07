package com.david.travel_booking_system.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ApiError {
    private HttpStatus httpStatus;
    private int httpStatusCode;
    private String errorMessage;
    private Map<String, String> errors;
    private String path;
    private LocalDateTime timestamp;

    public ApiError(HttpStatus httpStatus, int httpStatusCode, String errorMessage, Map<String, String> errors, String path) {
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
        this.errors = errors;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus httpStatus, int httpStatusCode, String errorMessage, String path) {
        this(httpStatus, httpStatusCode, errorMessage, null, path);
    }
}
