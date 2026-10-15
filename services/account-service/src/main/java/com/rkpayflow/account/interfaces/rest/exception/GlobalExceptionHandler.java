package com.rkpayflow.account.interfaces.rest.exception;

import com.rkpayflow.account.application.exception.AccountNotFoundException;
import com.rkpayflow.account.application.exception.DuplicateAccountNumberException;
import com.rkpayflow.account.interfaces.rest.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateAccountNumberException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateAccount(
            DuplicateAccountNumberException exception,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.CONFLICT,
                exception.getMessage(),
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAccountNotFound(
            AccountNotFoundException exception,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {

        Map<String, String> validationErrors =
                exception.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                fieldError -> fieldError.getDefaultMessage() == null
                                        ? "Invalid value"
                                        : fieldError.getDefaultMessage(),
                                (first, second) -> first));

        return buildError(
                HttpStatus.BAD_REQUEST,
                "Request validation failed",
                request.getRequestURI(),
                validationErrors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {

        return buildError(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                request.getRequestURI(),
                Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request) {

        // Do not expose internal exception details to API clients.
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred",
                request.getRequestURI(),
                Map.of());
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String message,
            String path,
            Map<String, String> validationErrors) {

        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validationErrors);

        return ResponseEntity.status(status).body(response);
    }
}
