package com.example.hospital_manager.security;

import com.example.hospital_manager.model.Role;
import org.springframework.security.core.Authentication;

/** Checks the caller's role inside services. */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean hasRole(Authentication auth, Role role) {
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.name()));
    }
}
