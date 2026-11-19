package com.example.hospital_manager.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;

/** JSON shape of every error. */
public record ErrorResponse(String timestamp, int status, String error, String message, String path) {

    public static ErrorResponse of(HttpStatus status, String message, String path) {
        return new ErrorResponse(Instant.now().toString(), status.value(), status.getReasonPhrase(), message, path);
    }
}
