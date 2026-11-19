package com.example.hospital_manager.service;

import com.example.hospital_manager.dto.AuthResponse;
import com.example.hospital_manager.dto.LoginRequest;
import com.example.hospital_manager.dto.RegisterRequest;
import com.example.hospital_manager.dto.UserResponse;
import com.example.hospital_manager.exception.ApiException;
import com.example.hospital_manager.model.AppUser;
import com.example.hospital_manager.model.Patient;
import com.example.hospital_manager.model.Role;
import com.example.hospital_manager.repository.UserRepository;
import com.example.hospital_manager.repository.PatientRepository;
import com.example.hospital_manager.security.JwtService;
import com.example.hospital_manager.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/** Login, register, "me", list users and doctors. */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SequenceGeneratorService sequence;

    public AuthResponse login(LoginRequest request) {
        // checks the BCrypt password; throws BadCredentialsException (-> 401) if wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        AppUser user = userRepository.findByUsername(request.username()).orElseThrow();
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, "Bearer", jwtService.getExpirationMs(),
                user.getUsername(), user.getName(), user.getRole());
    }

    /** Anyone can register as PATIENT. Other roles need an ADMIN token. */
    public UserResponse register(RegisterRequest request, Authentication caller) {
        Role role = request.role() == null ? Role.PATIENT : request.role();

        if (role != Role.PATIENT && !SecurityUtils.hasRole(caller, Role.ADMIN)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only an ADMIN can register " + role + " accounts");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists");
        }

        AppUser user = new AppUser(sequence.nextId("users"), request.username(),
                passwordEncoder.encode(request.password()), request.name(), role);
        userRepository.save(user);

        if (role == Role.PATIENT) {   // a patient account also gets a patient profile
            Patient patient = new Patient();
            patient.setId(sequence.nextId("patients"));
            patient.setName(user.getName());
            patient.setUsername(user.getUsername());
            patientRepository.save(patient);
        }
        return UserResponse.from(user);
    }

    public UserResponse me(String username) {
        return UserResponse.from(userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    public List<UserResponse> listDoctors() {
        return userRepository.findByRole(Role.DOCTOR).stream().map(UserResponse::from).toList();
    }
}
