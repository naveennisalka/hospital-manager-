package com.example.hospital_manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/** Patient details, with validation rules. */
@Getter
@Setter
@NoArgsConstructor
@Document("patients")
public class Patient {

    @Id
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @Past(message = "dateOfBirth must be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "gender must be MALE, FEMALE or OTHER")
    private String gender;

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "phone must have 9-15 digits")
    private String phone;

    @Email(message = "email is not valid")
    private String email;

    /** Username of the patient's login account (null if the patient has no account). */
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    public Patient(String name, LocalDate dateOfBirth, String gender, String phone, String email) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
    }
}
