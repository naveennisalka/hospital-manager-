package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.RegisterRequest;
import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StaffController {

    private final AuthService authService;

    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> users() {
        return authService.listUsers();
    }

    /** Available doctors - any logged-in user. */
    @GetMapping("/api/doctors")
    public List<UserResponse> doctors() {
        return authService.listDoctors();
    }

    /** Add a new doctor (the role is always DOCTOR). */
    @PostMapping("/api/admin/doctors")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addDoctor(@Valid @RequestBody RegisterRequest request, Authentication authentication) {
        RegisterRequest doctor = new RegisterRequest(request.username(), request.password(), request.name(), Role.DOCTOR);
        return authService.register(doctor, authentication);
    }
}
