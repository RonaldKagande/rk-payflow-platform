package com.rkpayflow.account.interfaces.rest.exception;

import com.rkpayflow.account.application.exception.AccountNotFoundException;
import com.rkpayflow.account.application.exception.DuplicateAccountNumberException;
import com.rkpayflow.account.interfaces.rest.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();

        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/accounts");
    }

    @Test
    void shouldReturn409WhenAccountNumberAlreadyExists() {
        var exception =
                new DuplicateAccountNumberException("123456789");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleDuplicateAccount(exception, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().status());
        assertEquals("Conflict", response.getBody().error());
        assertTrue(response.getBody().message()
                .contains("123456789"));
        assertEquals("/api/accounts", response.getBody().path());
        assertTrue(response.getBody().validationErrors().isEmpty());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void shouldReturn404WhenAccountDoesNotExist() {
        UUID accountId = UUID.randomUUID();
        var exception = new AccountNotFoundException(accountId);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleAccountNotFound(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().status());
        assertTrue(response.getBody().message()
                .contains(accountId.toString()));
        assertEquals("/api/accounts", response.getBody().path());
    }

    @Test
    void shouldReturn400WithFieldErrorsWhenValidationFails() {
        BindingResult bindingResult = mock(BindingResult.class);

        FieldError emailError = new FieldError(
                "openAccountRequest",
                "email",
                "Email must be valid"
        );

        FieldError nameError = new FieldError(
                "openAccountRequest",
                "customerName",
                "Customer name is required"
        );

        when(bindingResult.getFieldErrors())
                .thenReturn(List.of(emailError, nameError));

        MethodArgumentNotValidException exception =
                mock(MethodArgumentNotValidException.class);

        when(exception.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<ApiErrorResponse> response =
                handler.handleValidationErrors(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("Request validation failed",
                response.getBody().message());

        assertEquals(
                "Email must be valid",
                response.getBody().validationErrors().get("email")
        );

        assertEquals(
                "Customer name is required",
                response.getBody().validationErrors().get("customerName")
        );
    }

    @Test
    void shouldReturn400WhenArgumentIsIllegal() {
        var exception =
                new IllegalArgumentException("Unsupported currency");

        ResponseEntity<ApiErrorResponse> response =
                handler.handleIllegalArgument(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unsupported currency",
                response.getBody().message());
        assertTrue(response.getBody().validationErrors().isEmpty());
    }

    @Test
    void shouldReturnGeneric500WithoutExposingInternalDetails() {
        var exception = new RuntimeException(
                "Database password or SQL details must not leak"
        );

        ResponseEntity<ApiErrorResponse> response =
                handler.handleUnexpectedException(exception, request);

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals(
                "An unexpected error occurred",
                response.getBody().message()
        );

        assertFalse(response.getBody().message()
                .contains("Database password"));

        assertEquals("/api/accounts", response.getBody().path());
    }
}
