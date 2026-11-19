package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.MedicalRecordRequest;
import com.example.hospital_manager.dto.MedicalRecordResponse;
import com.example.hospital_manager.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /** Doctor writes a diagnosis / prescription. */
    @PostMapping("/api/records")
    @PreAuthorize("hasRole('DOCTOR')")
    @ResponseStatus(HttpStatus.CREATED)
    public MedicalRecordResponse create(@Valid @RequestBody MedicalRecordRequest request, Authentication auth) {
        return medicalRecordService.create(request, auth);
    }

    @GetMapping("/api/records/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    public List<MedicalRecordResponse> forPatient(@PathVariable Long patientId) {
        return medicalRecordService.listForPatient(patientId);
    }
}
