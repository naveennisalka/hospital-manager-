package com.example.hospital_manager.model;

/**
 * System roles. Spring Security sees each one as an authority with the
 * "ROLE_" prefix (e.g. ROLE_ADMIN), which is what hasRole('ADMIN') checks.
 */
public enum Role {
    ADMIN,
    DOCTOR,
    NURSE,
    RECEPTIONIST,
    PATIENT
}
