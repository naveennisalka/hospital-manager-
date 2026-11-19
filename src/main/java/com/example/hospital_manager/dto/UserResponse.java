package com.example.hospital_manager.dto;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Role;

/** A user shown to clients - no password. */
public record UserResponse(Long id, String username, String name, Role role) {

    public static UserResponse from(AppUser user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getName(), user.getRole());
    }
}
