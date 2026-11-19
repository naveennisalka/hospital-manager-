package com.example.hospital_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicalRecordRequest(
        @NotNull(message = "patientId is required") Long patientId,
        @NotBlank(message = "diagnosis is required") String diagnosis,
        String prescription) {
}
