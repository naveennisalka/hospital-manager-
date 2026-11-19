package com.example.hospital_manager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/** Diagnosis and prescription written by a doctor. */
@Getter
@Setter
@NoArgsConstructor
@Document("medical_records")
public class MedicalRecord {

    @Id
    private Long id;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private String diagnosis;
    private String prescription;
    private LocalDateTime createdAt = LocalDateTime.now();
}
