package com.example.hospital_manager.controller;

import com.example.hospital_manager.dto.AppointmentRequest;
import com.example.hospital_manager.dto.AppointmentResponse;
import com.example.hospital_manager.dto.StatusRequest;
import com.example.hospital_manager.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    /** Patient books a doctor appointment for themselves. */
    @PostMapping("/api/patients/appointments")
    @PreAuthorize("hasRole('PATIENT')")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse bookByPatient(@Valid @RequestBody AppointmentRequest request, Authentication auth) {
        return appointmentService.book(request, auth);
    }

    /** Patient views their own appointments. */
    @GetMapping("/api/patients/appointments")
    @PreAuthorize("hasRole('PATIENT')")
    public List<AppointmentResponse> patientAppointments(Authentication auth) {
        return appointmentService.listForPatient(auth);
    }

    /** Doctor views their appointments (admin sees all). */
    @GetMapping("/api/doctors/appointments")
    @PreAuthorize("hasAnyRole('DOCTOR','ADMIN')")
    public List<AppointmentResponse> doctorAppointments(Authentication auth) {
        return appointmentService.listForDoctor(auth);
    }

    /** Staff book for a patient (patientId required). */
    @PostMapping("/api/appointments")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
    @ResponseStatus(HttpStatus.CREATED)
    public AppointmentResponse bookByStaff(@Valid @RequestBody AppointmentRequest request, Authentication auth) {
        return appointmentService.book(request, auth);
    }

    @GetMapping("/api/appointments")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','NURSE')")
    public List<AppointmentResponse> all() {
        return appointmentService.listAll();
    }

    @PatchMapping("/api/appointments/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST','DOCTOR')")
    public AppointmentResponse updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request,
                                            Authentication auth) {
        return appointmentService.updateStatus(id, request, auth);
    }
}
