package com.example.hospital_manager.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** 401 - no token was sent to a protected URL. */
@Component
@RequiredArgsConstructor
public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ErrorResponseWriter errorWriter;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        errorWriter.write(response, HttpStatus.UNAUTHORIZED,
                "Login required - send Authorization: Bearer <token>", request.getRequestURI());
    }
}
