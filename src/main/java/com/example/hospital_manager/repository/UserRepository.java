package com.example.hospital_manager.repository;

import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);

    List<AppUser> findByRole(Role role);
}
