package com.example.hospital_manager.repository;

import com.example.hospital_manager.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends MongoRepository<Patient, Long> {

    List<Patient> findByNameContainingIgnoreCase(String name);

    Optional<Patient> findByUsername(String username);
}
