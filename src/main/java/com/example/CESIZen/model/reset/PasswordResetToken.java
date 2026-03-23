package com.example.CESIZen.model.reset;

import com.example.CESIZen.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pin;
    private LocalDateTime expiration;
    @Column(nullable = false)
    private boolean used = false;
    @Column(nullable = false)
    private int attempts = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}

