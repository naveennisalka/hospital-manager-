package com.example.hospital_manager.repository;

import com.example.hospital_manager.model.MedicalRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MedicalRecordRepository extends MongoRepository<MedicalRecord, Long> {

    List<MedicalRecord> findByPatientId(Long patientId);

    boolean existsByPatientId(Long patientId);
}
