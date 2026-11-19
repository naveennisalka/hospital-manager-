package com.example.hospital_manager.security;

import com.example.hospital_manager.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Writes the ErrorResponse JSON from inside the security filter chain. */
@Component
public class ErrorResponseWriter {

    public void write(HttpServletResponse response, HttpStatus status, String message, String path)
            throws IOException {
        ErrorResponse e = ErrorResponse.of(status, message, path);
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("""
                {"timestamp":"%s","status":%d,"error":"%s","message":"%s","path":"%s"}"""
                .formatted(e.timestamp(), e.status(), e.error(), e.message(), e.path()));
    }
}
