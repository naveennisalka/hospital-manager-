package com.example.hospital_manager.config;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.repository.UserRepository;
import com.example.hospital_manager.service.SequenceGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/** Creates the seeded accounts and sample patients on the first start (when there are no users). */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final SequenceGeneratorService sequence;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }
        addUser("admin", "admin123", "System Admin", Role.ADMIN);
        addUser("doctor", "doctor123", "Dr. Nimal Silva", Role.DOCTOR);
        addUser("nurse", "nurse123", "Nurse Dilani Fernando", Role.NURSE);
        addUser("reception", "reception123", "Kasun Perera", Role.RECEPTIONIST);

        addPatient(new Patient("Amaya Wickramasinghe", LocalDate.of(1995, 9, 3), "FEMALE", "0712345678", "amaya@example.com"));
        addPatient(new Patient("Ruwan Bandara", LocalDate.of(1988, 1, 21), "MALE", "0759876543", "ruwan@example.com"));

        System.out.println("Seed data created: admin/admin123, doctor/doctor123, nurse/nurse123, reception/reception123");
    }

    private void addUser(String username, String password, String name, Role role) {
        userRepository.save(new AppUser(sequence.nextId("users"), username,
                passwordEncoder.encode(password), name, role));
    }

    private void addPatient(Patient patient) {
        patient.setId(sequence.nextId("patients"));
        patientRepository.save(patient);
    }
}
