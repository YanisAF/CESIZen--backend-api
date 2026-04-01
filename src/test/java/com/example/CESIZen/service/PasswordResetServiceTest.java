package com.example.CESIZen.service;

import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.model.reset.PasswordResetToken;
import com.example.CESIZen.model.user.User;
import com.example.CESIZen.repository.PasswordResetTokenRepository;
import com.example.CESIZen.repository.UserRepository;
import com.example.CESIZen.service.event.EventService;
import com.example.CESIZen.service.reset.JwtResetTokenService;
import com.example.CESIZen.service.reset.PasswordResetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - PasswordResetService")
class PasswordResetServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EventService eventService;
    @Mock private JwtResetTokenService jwtResetTokenService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private User user;
    private PasswordResetToken validToken;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUserName("johndoe");
        user.setEmail("john.doe@example.com");
        user.setPhone("0612345678");
        user.setPassword("$2a$10$encoded");

        validToken = new PasswordResetToken();
        validToken.setId(1L);
        validToken.setUser(user);
        validToken.setPin("$2a$10$encodedPin");
        validToken.setExpiration(LocalDateTime.now().plusMinutes(5));
        validToken.setUsed(false);
        validToken.setAttempts(0);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  requestReset()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RST-01 | requestReset() - Succès : token créé et événement publié")
    void requestReset_shouldSaveTokenAndPublishEvent() throws Exception {
        when(userRepository.findByEmailOrPhone("john.doe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPin");
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(validToken);
        doNothing().when(eventService).publisherResetEvent(any(), anyString(), anyString());

        assertThatNoException().isThrownBy(
                () -> passwordResetService.requestReset("john.doe@example.com", "EMAIL")
        );

        verify(tokenRepository).save(any(PasswordResetToken.class));
        verify(eventService).publisherResetEvent(eq(user), anyString(), eq("EMAIL"));
    }

    @Test
    @DisplayName("RST-02 | requestReset() - Succès : reset via numéro de téléphone")
    void requestReset_shouldWork_withPhoneIdentifier() throws Exception {
        when(userRepository.findByEmailOrPhone("0612345678")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$encodedPin");
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(validToken);
        doNothing().when(eventService).publisherResetEvent(any(), anyString(), anyString());

        assertThatNoException().isThrownBy(
                () -> passwordResetService.requestReset("0612345678", "SMS")
        );

        verify(eventService).publisherResetEvent(eq(user), anyString(), eq("SMS"));
    }

    @Test
    @DisplayName("RST-03 | requestReset() - Échec : utilisateur introuvable")
    void requestReset_shouldThrow_whenUserNotFound() {
        when(userRepository.findByEmailOrPhone("unknown@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordResetService.requestReset("unknown@example.com", "EMAIL"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("introuvable");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  verifyPin()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RST-04 | verifyPin() - Succès : PIN valide, JWT retourné")
    void verifyPin_shouldReturnJwt_whenPinIsValid() throws ResourceNotFoundException{
        when(userRepository.findByEmailOrPhone("john.doe@example.com")).thenReturn(Optional.of(user));
        when(tokenRepository.findFirstByUserAndUsedFalseOrderByExpirationDesc(user))
                .thenReturn(Optional.of(validToken));
        when(passwordEncoder.matches("123456", "$2a$10$encodedPin")).thenReturn(true);
        when(jwtResetTokenService.generateResetToken(user)).thenReturn("jwt-reset-token");

        String jwt = passwordResetService.verifyPin("john.doe@example.com", "123456");

        assertThat(jwt).isEqualTo("jwt-reset-token");
        assertThat(validToken.isUsed()).isTrue();
    }

    @Test
    @DisplayName("RST-05 | verifyPin() - Échec : PIN invalide, compteur incrémenté")
    void verifyPin_shouldIncrementAttempts_whenPinIsWrong() {
        when(userRepository.findByEmailOrPhone("john.doe@example.com")).thenReturn(Optional.of(user));
        when(tokenRepository.findFirstByUserAndUsedFalseOrderByExpirationDesc(user))
                .thenReturn(Optional.of(validToken));
        when(passwordEncoder.matches("000000", "$2a$10$encodedPin")).thenReturn(false);

        assertThatThrownBy(() -> passwordResetService.verifyPin("john.doe@example.com", "000000"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("invalide");

        assertThat(validToken.getAttempts()).isEqualTo(1);
    }

    @Test
    @DisplayName("RST-06 | verifyPin() - Échec : PIN expiré")
    void verifyPin_shouldThrow_whenPinIsExpired() {
        validToken.setExpiration(LocalDateTime.now().minusMinutes(10));

        when(userRepository.findByEmailOrPhone("john.doe@example.com")).thenReturn(Optional.of(user));
        when(tokenRepository.findFirstByUserAndUsedFalseOrderByExpirationDesc(user))
                .thenReturn(Optional.of(validToken));

        assertThatThrownBy(() -> passwordResetService.verifyPin("john.doe@example.com", "123456"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("expiré");
    }

    @Test
    @DisplayName("RST-07 | verifyPin() - Échec : nombre max de tentatives atteint")
    void verifyPin_shouldThrow_whenMaxAttemptsReached() {
        validToken.setAttempts(3);

        when(userRepository.findByEmailOrPhone("john.doe@example.com")).thenReturn(Optional.of(user));
        when(tokenRepository.findFirstByUserAndUsedFalseOrderByExpirationDesc(user))
                .thenReturn(Optional.of(validToken));

        assertThatThrownBy(() -> passwordResetService.verifyPin("john.doe@example.com", "123456"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("tentatives");
    }

    @Test
    @DisplayName("RST-08 | verifyPin() - Échec : aucun PIN actif pour l'utilisateur")
    void verifyPin_shouldThrow_whenNoPinActive() {
        when(userRepository.findByEmailOrPhone("john.doe@example.com")).thenReturn(Optional.of(user));
        when(tokenRepository.findFirstByUserAndUsedFalseOrderByExpirationDesc(user))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordResetService.verifyPin("john.doe@example.com", "123456"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("PIN actif");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  resetPassword()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RST-09 | resetPassword() - Succès : mot de passe réinitialisé")
    void resetPassword_shouldUpdatePassword() throws Exception {
        when(jwtResetTokenService.extractUsername("valid-jwt")).thenReturn("john.doe@example.com");
        when(userRepository.getByEmail("john.doe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NewPass!123")).thenReturn("$2a$10$newEncoded");
        doNothing().when(eventService).publisherConfirmationEvent(any(), anyString());

        assertThatNoException().isThrownBy(
                () -> passwordResetService.resetPassword("valid-jwt", "NewPass!123", "EMAIL")
        );

        verify(eventService).publisherConfirmationEvent(eq(user), eq("EMAIL"));
        assertThat(user.getPassword()).isEqualTo("$2a$10$newEncoded");
    }

    @Test
    @DisplayName("RST-10 | resetPassword() - Échec : utilisateur introuvable via JWT")
    void resetPassword_shouldThrow_whenUserNotFoundByEmail() {
        when(jwtResetTokenService.extractUsername("valid-jwt")).thenReturn("ghost@example.com");
        when(userRepository.getByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordResetService.resetPassword("valid-jwt", "NewPass!123", "EMAIL"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("introuvable");
    }
}
