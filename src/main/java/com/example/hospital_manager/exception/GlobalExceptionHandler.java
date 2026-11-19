package com.example.hospital_manager.exception;

import com.example.hospital_manager.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/** Turns exceptions into JSON: 400, 401, 403, 404, 409, 415, 500. */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> api(ApiException ex, HttpServletRequest req) {
        return error(ex.getStatus(), ex.getMessage(), req);
    }

    // 400 - @Valid failed, e.g. "name: name is required"
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return error(HttpStatus.BAD_REQUEST, message, req);
    }

    // 400 - broken JSON or unknown enum value
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> badJson(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return error(HttpStatus.BAD_REQUEST, "Invalid JSON body", req);
    }

    // 415 - body not sent as JSON
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> notJson(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return error(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Send the body as JSON (Body -> raw -> JSON)", req);
    }

    // 401 - wrong username or password
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> badLogin(AuthenticationException ex, HttpServletRequest req) {
        return error(HttpStatus.UNAUTHORIZED, "Invalid username or password", req);
    }

    // 403 - @PreAuthorize rejected the role
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> denied(AccessDeniedException ex, HttpServletRequest req) {
        return error(HttpStatus.FORBIDDEN, "Access denied for your role", req);
    }

    // anything else: Spring's own errors (404 unknown URL, 405 wrong method) keep their status, the rest is 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> other(Exception ex, HttpServletRequest req) {
        if (ex instanceof org.springframework.web.ErrorResponse springError) {
            return error(HttpStatus.valueOf(springError.getStatusCode().value()), ex.getMessage(), req);
        }
        ex.printStackTrace();
        return error(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), req);
    }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(ErrorResponse.of(status, message, req.getRequestURI()));
    }
}
