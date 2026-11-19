package com.example.hospital_manager.dto;

import com.example.hospital_manager.model.MedicalRecord;

import java.time.LocalDateTime;

public record MedicalRecordResponse(Long id, Long patientId, String patientName, Long doctorId, String doctorName,
                                    String diagnosis, String prescription, LocalDateTime createdAt) {

    public static MedicalRecordResponse from(MedicalRecord r) {
        return new MedicalRecordResponse(r.getId(), r.getPatientId(), r.getPatientName(), r.getDoctorId(),
                r.getDoctorName(), r.getDiagnosis(), r.getPrescription(), r.getCreatedAt());
    }
}
