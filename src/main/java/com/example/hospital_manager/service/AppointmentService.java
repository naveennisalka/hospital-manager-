package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.AppointmentRequest;
import com.example.hospital_manager.dto.AppointmentResponse;
import com.example.hospital_manager.dto.StatusRequest;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Appointment;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.AppointmentRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import com.example.hospital_manager.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Booking and listing appointments.
 * Ownership rule: a patient books only for themselves; a doctor only sees and changes their own appointments.
 */
@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final SequenceGeneratorService sequence;

    public AppointmentResponse book(AppointmentRequest request, Authentication caller) {
        Patient patient;
        if (SecurityUtils.hasRole(caller, Role.PATIENT)) {
            patient = patientRepository.findByUsername(caller.getName())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient profile not found"));
        } else {
            if (request.patientId() == null) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "patientId is required");
            }
            patient = patientRepository.findById(request.patientId())
                    .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient not found"));
        }

        AppUser doctor = userRepository.findById(request.doctorId())
                .filter(u -> u.getRole() == Role.DOCTOR)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Doctor not found"));

        Appointment a = new Appointment();
        a.setId(sequence.nextId("appointments"));
        a.setPatientId(patient.getId());
        a.setPatientName(patient.getName());
        a.setDoctorId(doctor.getId());
        a.setDoctorName(doctor.getName());
        a.setAppointmentTime(request.appointmentTime());
        a.setReason(request.reason());
        return AppointmentResponse.from(appointmentRepository.save(a));
    }

    public List<AppointmentResponse> listAll() {
        return appointmentRepository.findAll().stream().map(AppointmentResponse::from).toList();
    }

    /** A doctor sees only their own appointments; an admin sees all. */
    public List<AppointmentResponse> listForDoctor(Authentication caller) {
        if (!SecurityUtils.hasRole(caller, Role.DOCTOR)) {
            return listAll();
        }
        Long doctorId = currentUser(caller).getId();
        return appointmentRepository.findByDoctorId(doctorId).stream().map(AppointmentResponse::from).toList();
    }

    /** The logged-in patient's own appointments. */
    public List<AppointmentResponse> listForPatient(Authentication caller) {
        Patient patient = patientRepository.findByUsername(caller.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Patient profile not found"));
        return appointmentRepository.findByPatientId(patient.getId()).stream().map(AppointmentResponse::from).toList();
    }

    public AppointmentResponse updateStatus(Long id, StatusRequest request, Authentication caller) {
        Appointment a = appointmentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Appointment " + id + " not found"));

        if (SecurityUtils.hasRole(caller, Role.DOCTOR) && !a.getDoctorId().equals(currentUser(caller).getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "This appointment belongs to another doctor");
        }
        a.setStatus(request.status());
        return AppointmentResponse.from(appointmentRepository.save(a));
    }

    private AppUser currentUser(Authentication caller) {
        return userRepository.findByUsername(caller.getName()).orElseThrow();
    }
}
