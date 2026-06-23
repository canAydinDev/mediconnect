package com.canaydin.mediconnect.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        exception.getConstraintViolations()
                .forEach(constraintViolation -> {
                    String propertyPath = constraintViolation.getPropertyPath().toString();

                    String fieldName = propertyPath.contains(".")
                            ? propertyPath.substring(propertyPath.lastIndexOf(".") + 1)
                            : propertyPath;

                    errors.put(fieldName, constraintViolation.getMessage());
                });

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(
            MethodArgumentNotValidException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errors.put(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()
                ));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(
            HandlerMethodValidationException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        exception.getParameterValidationResults()
                .forEach(parameterValidationResult -> {
                    String parameterName = parameterValidationResult
                            .getMethodParameter()
                            .getParameterName();

                    String errorMessage = parameterValidationResult
                            .getResolvableErrors()
                            .stream()
                            .map(error -> error.getDefaultMessage())
                            .collect(Collectors.joining(", "));

                    errors.put(parameterName, errorMessage);
                });

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest
    ){
        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND.toString(),
                exception.getMessage(),
                Instant.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception exception,
            WebRequest webRequest
    ){
        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                exception.getMessage(),
                Instant.now()
        );
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }



}
