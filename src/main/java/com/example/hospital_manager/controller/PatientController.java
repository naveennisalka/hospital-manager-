package com.example.hospital_manager.controller;

import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    /** List all registered patients (admin). */
    @GetMapping("/api/admin/patients")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Patient> adminList() {
        return patientService.list(null);
    }

    /** List or search patients (?name=...). Staff only. */
    @GetMapping("/api/patients")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
    public List<Patient> list(@RequestParam(required = false) String name) {
        return patientService.list(name);
    }

    @GetMapping("/api/patients/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
    public Patient get(@PathVariable Long id) {
        return patientService.get(id);
    }

    @PostMapping("/api/patients")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@Valid @RequestBody Patient patient) {
        return patientService.create(patient);
    }

    @PutMapping("/api/patients/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    public Patient update(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        return patientService.update(id, patient);
    }

    @DeleteMapping("/api/patients/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        patientService.delete(id);
    }
}
