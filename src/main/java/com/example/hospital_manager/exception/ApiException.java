package com.example.hospital_manager.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/** Thrown by services; carries the HTTP status to return (404, 409, 403 ...). */
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
