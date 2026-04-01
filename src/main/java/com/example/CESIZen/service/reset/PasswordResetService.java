package com.example.CESIZen.service.reset;

import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.model.reset.PasswordResetToken;
import com.example.CESIZen.model.user.User;
import com.example.CESIZen.repository.PasswordResetTokenRepository;
import com.example.CESIZen.repository.UserRepository;
import com.example.CESIZen.service.event.EventService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventService eventService;
    private final JwtResetTokenService jwtResetTokenService;

    private static final int MAX_ATTEMPTS = 3;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder,
                                EventService eventService,
                                JwtResetTokenService jwtResetTokenService
    ) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventService = eventService;
        this.jwtResetTokenService = jwtResetTokenService;
    }

    public void requestReset(String identifier, String channel) throws ResourceNotFoundException {
        identifier = identifier.toLowerCase();

        User user = userRepository.findByEmailOrPhone(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        String rawPin = generatePin();

        PasswordResetToken token = new PasswordResetToken();
        token.setPin(passwordEncoder.encode(rawPin));
        token.setExpiration(LocalDateTime.now().plusMinutes(5));
        token.setUser(user);

        tokenRepository.save(token);
        eventService.publisherResetEvent(user, rawPin, channel);
    }

    public String verifyPin(String identifier, String pin) throws ResourceNotFoundException {
        identifier = identifier.toLowerCase();

        User user = userRepository.findByEmailOrPhone(identifier)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        PasswordResetToken token = tokenRepository.findFirstByUserAndUsedFalseOrderByExpirationDesc(user)
                .orElseThrow(() -> new ResourceNotFoundException("Aucun PIN actif"));

        if (token.getExpiration().isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("PIN expiré");
        }

        if (token.getAttempts() >= MAX_ATTEMPTS) {
            throw new ResourceNotFoundException("Nombre maximal de tentatives atteint");
        }

        if (!passwordEncoder.matches(pin, token.getPin())) {
            token.setAttempts(token.getAttempts() + 1);
            throw new ResourceNotFoundException("PIN invalide");
        }

        token.setUsed(true);

        return jwtResetTokenService.generateResetToken(user);
    }

    public void resetPassword(String jwt, String newPassword, String channel) throws Exception{

        String email = jwtResetTokenService.extractUsername(jwt);

        User user = userRepository.getByEmail(email)
                .orElseThrow(() -> new Exception("Utilisateur introuvable"));

        user.setPassword(passwordEncoder.encode(newPassword));
        eventService.publisherConfirmationEvent(user, channel);
    }

    private String generatePin() {
        return String.valueOf(
                new SecureRandom().nextInt(900000) + 100000
        );
    }
}
