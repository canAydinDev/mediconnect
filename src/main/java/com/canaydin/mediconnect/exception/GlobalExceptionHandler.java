package com.canaydin.mediconnect.exception;

import io.micrometer.tracing.Tracer;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final Tracer tracer;

    @ExceptionHandler(InvalidEnumValueException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnumValueException(
            InvalidEnumValueException exception,
            WebRequest webRequest
    ) {
        ErrorResponse errorResponse = buildErrorResponse(
                webRequest,
                HttpStatus.BAD_REQUEST,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        exception.getConstraintViolations()
                .forEach(constraintViolation -> {
                    String propertyPath = constraintViolation
                            .getPropertyPath()
                            .toString();

                    String fieldName = propertyPath.contains(".")
                            ? propertyPath.substring(propertyPath.lastIndexOf(".") + 1)
                            : propertyPath;

                    errors.put(
                            fieldName,
                            constraintViolation.getMessage()
                    );
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

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(
            DuplicateResourceException exception,
            WebRequest webRequest
    ) {
        ErrorResponse errorResponse = buildErrorResponse(
                webRequest,
                HttpStatus.CONFLICT,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(WeakPasswordException.class)
    public ResponseEntity<Map<String, String>> handleWeakPasswordException(
            WeakPasswordException exception
    ) {
        Map<String, String> errors = new HashMap<>();

        errors.put(
                "password",
                exception.getMessage()
        );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest
    ) {
        ErrorResponse errorResponse = buildErrorResponse(
                webRequest,
                HttpStatus.NOT_FOUND,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(BusinessConflictException.class)
    public ResponseEntity<ErrorResponse> handleBusinessConflictException(
            BusinessConflictException exception,
            WebRequest webRequest
    ) {
        ErrorResponse errorResponse = buildErrorResponse(
                webRequest,
                HttpStatus.CONFLICT,
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception exception,
            WebRequest webRequest
    ) {
        String traceId = getCurrentTraceId();

        log.error(
                "Unexpected error occurred. traceId={}",
                traceId,
                exception
        );

        ErrorResponse errorResponse = new ErrorResponse(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "An unexpected error occurred.",
                Instant.now(),
                traceId
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    private ErrorResponse buildErrorResponse(
            WebRequest webRequest,
            HttpStatus status,
            String message
    ) {
        return new ErrorResponse(
                webRequest.getDescription(false),
                status.toString(),
                message,
                Instant.now(),
                getCurrentTraceId()
        );
    }

    private String getCurrentTraceId() {
        var span = tracer.currentSpan();

        return span != null
                ? span.context().traceId()
                : null;
    }
}