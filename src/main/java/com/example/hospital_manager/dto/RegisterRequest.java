package com.example.hospital_manager.dto;

import com.example.hospital_manager.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** If role is missing it defaults to PATIENT. */
public record RegisterRequest(
        @NotBlank(message = "username is required")
        @Size(min = 3, max = 50, message = "username must be 3-50 characters") String username,

        @NotBlank(message = "password is required")
        @Size(min = 6, message = "password must be at least 6 characters") String password,

        @NotBlank(message = "name is required") String name,

        Role role) {
}
