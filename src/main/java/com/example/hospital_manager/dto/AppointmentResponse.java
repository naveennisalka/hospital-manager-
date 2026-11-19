package com.example.hospital_manager.dto;

import com.example.hospital_manager.model.Appointment;
import com.example.hospital_manager.model.AppointmentStatus;

import java.time.LocalDateTime;

public record AppointmentResponse(Long id, Long patientId, String patientName, Long doctorId, String doctorName,
                                  LocalDateTime appointmentTime, String reason, AppointmentStatus status) {

    public static AppointmentResponse from(Appointment a) {
        return new AppointmentResponse(a.getId(), a.getPatientId(), a.getPatientName(), a.getDoctorId(),
                a.getDoctorName(), a.getAppointmentTime(), a.getReason(), a.getStatus());
    }
}
