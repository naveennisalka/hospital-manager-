package com.example.hospital_manager.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/** A login account. The password field holds a BCrypt hash, never plain text. */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class AppUser {

    @Id
    private Long id;

    @Indexed(unique = true)
    private String username;

    private String password;

    private String name;

    private Role role;
}
