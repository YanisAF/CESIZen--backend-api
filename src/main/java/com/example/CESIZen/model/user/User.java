package com.example.CESIZen.model.user;

import com.example.CESIZen.enums.Roles;
import com.example.CESIZen.model.quiz.ResultDiagnosis;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ce champ est obligatoire")
    @JsonProperty("user_name")
    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Ce champ est obligatoire")
    @JsonProperty("email")
    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @JsonProperty("phone")
    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "Ce champ est obligatoire")
    @JsonProperty("password")
    @Column(name = "password", nullable = false)
    private String password;

    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    private Roles role;

    @JsonProperty("last_activity_at")
    private LocalDateTime lastActivityAt = LocalDateTime.now();

    @JsonProperty("is_active")
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @JsonProperty("original_username")
    @Column(name = "original_username")
    private String originalUsername;

    @JsonProperty("original_email")
    @Column(name = "original_email")
    private String originalEmail;

    @JsonProperty("anonymized_at")
    private LocalDateTime anonymizedAt;

    @OneToMany(mappedBy = "user")
    private List<ResultDiagnosis> resultsDiagnosis;
}

