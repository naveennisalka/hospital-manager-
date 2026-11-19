package com.example.hospital_manager.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/** Links a patient and a doctor at a date and time. */
@Getter
@Setter
@NoArgsConstructor
@Document("appointments")
public class Appointment {

    @Id
    private Long id;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private LocalDateTime appointmentTime;
    private String reason;
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;
}
