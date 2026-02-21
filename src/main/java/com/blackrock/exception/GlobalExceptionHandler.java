package com.blackrock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildResponse(String message, String details, HttpStatus status) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", message);
        body.put("details", details);
        body.put("status", status.value());
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, status);
    }

    // -----------------------------------------
    // JSON parse issues (invalid date, bad JSON)
    // -----------------------------------------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonParseError(HttpMessageNotReadableException ex) {

        String originalMessage = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        return buildResponse(
                "Invalid request body",
                originalMessage,
                HttpStatus.BAD_REQUEST
        );
    }

    // -----------------------------------------
    // Custom thrown exceptions (DateUtils.parse)
    // -----------------------------------------
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArg(IllegalArgumentException ex) {
        return buildResponse(
                "Invalid input",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // -----------------------------------------
    // Invalid date format
    // -----------------------------------------
    @ExceptionHandler({DateTimeParseException.class, DateTimeException.class})
    public ResponseEntity<Object> handleDateErrors(DateTimeException ex) {
        return buildResponse(
                "Invalid datetime format. Required: yyyy-MM-dd HH:mm:ss",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    // -----------------------------------------
    // Validation errors (if using @Valid)
    // -----------------------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {

        String details = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return buildResponse(
                "Validation failed",
                details,
                HttpStatus.BAD_REQUEST
        );
    }

    // -----------------------------------------
    // Generic fallback handler
    // -----------------------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        ex.printStackTrace(); // Optional: helpful for debugging
        return buildResponse(
                "Internal server error",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}