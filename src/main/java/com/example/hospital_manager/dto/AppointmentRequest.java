package com.example.hospital_manager.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** patientId is only needed when staff book for a patient. */
public record AppointmentRequest(
        @NotNull(message = "doctorId is required") Long doctorId,
        Long patientId,
        @NotNull(message = "appointmentTime is required")
        @Future(message = "appointmentTime must be in the future") LocalDateTime appointmentTime,
        String reason) {
}
