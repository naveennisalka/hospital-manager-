package com.example.hospital_manager.service;

import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.AppointmentRepository;
import com.example.hospital_manager.repository.MedicalRecordRepository;
import com.example.hospital_manager.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/** Patient CRUD. A patient who has appointments or records cannot be deleted. */
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final SequenceGeneratorService sequence;

    public List<Patient> list(String name) {
        return name == null ? patientRepository.findAll() : patientRepository.findByNameContainingIgnoreCase(name);
    }

    public Patient get(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient " + id + " not found"));
    }

    public Patient create(Patient patient) {
        patient.setId(sequence.nextId("patients"));
        return patientRepository.save(patient);
    }

    public Patient update(Long id, Patient changes) {
        Patient patient = get(id);
        patient.setName(changes.getName());
        patient.setDateOfBirth(changes.getDateOfBirth());
        patient.setGender(changes.getGender());
        patient.setPhone(changes.getPhone());
        patient.setEmail(changes.getEmail());
        return patientRepository.save(patient);
    }

    public void delete(Long id) {
        Patient patient = get(id);
        if (appointmentRepository.existsByPatientId(id) || medicalRecordRepository.existsByPatientId(id)) {
            throw new ApiException(HttpStatus.CONFLICT, "Patient " + id + " has appointments or records");
        }
        patientRepository.delete(patient);
    }
}
