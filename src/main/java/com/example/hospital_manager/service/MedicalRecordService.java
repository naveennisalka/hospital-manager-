package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.MedicalRecordRequest;
import com.example.hospital_manager.dto.MedicalRecordResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.MedicalRecord;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.repository.MedicalRecordRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/** Medical records. The doctor is always taken from the token, never from the request body. */
@Service
@RequiredArgsConstructor
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final SequenceGeneratorService sequence;

    public MedicalRecordResponse create(MedicalRecordRequest request, Authentication caller) {
        AppUser doctor = userRepository.findByUsername(caller.getName()).orElseThrow();
        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));

        MedicalRecord r = new MedicalRecord();
        r.setId(sequence.nextId("medical_records"));
        r.setPatientId(patient.getId());
        r.setPatientName(patient.getName());
        r.setDoctorId(doctor.getId());
        r.setDoctorName(doctor.getName());
        r.setDiagnosis(request.diagnosis());
        r.setPrescription(request.prescription());
        return MedicalRecordResponse.from(medicalRecordRepository.save(r));
    }

    public List<MedicalRecordResponse> listForPatient(Long patientId) {
        return medicalRecordRepository.findByPatientId(patientId).stream().map(MedicalRecordResponse::from).toList();
    }
}
