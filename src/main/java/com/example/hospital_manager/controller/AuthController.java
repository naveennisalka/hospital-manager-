package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.AuthResponse;
import com.example.hospital_manager.dto.LoginRequest;
import com.example.hospital_manager.dto.RegisterRequest;
import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request, Authentication authentication) {
        return authService.register(request, authentication);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {
        return authService.me(authentication.getName());
    }
}
