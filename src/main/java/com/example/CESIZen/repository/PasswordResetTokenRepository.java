package com.example.CESIZen.repository;

import com.example.CESIZen.model.reset.PasswordResetToken;
import com.example.CESIZen.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findFirstByUserAndUsedFalseOrderByExpirationDesc(User user);
}
