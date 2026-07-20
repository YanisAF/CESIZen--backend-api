package com.example.CESIZen.service;

import com.example.CESIZen.model.user.User;
import com.example.CESIZen.service.reset.JwtResetTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Tests unitaires - JwtResetTokenService")
class JwtResetTokenServiceTest {

    // Clé de test (>= 256 bits pour HS256)
    private static final String TEST_SECRET = "0123456789abcdef0123456789abcdef0123456789abcdef";

    private JwtResetTokenService jwtResetTokenService;
    private User user;

    @BeforeEach
    void setUp() {
        jwtResetTokenService = new JwtResetTokenService(TEST_SECRET);

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  generateResetToken()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("JWT-01 | generateResetToken() - Succès : un token non vide est généré")
    void generateResetToken_shouldReturnNonBlankToken() {
        String token = jwtResetTokenService.generateResetToken(user);

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("JWT-02 | generateResetToken() - Succès : le subject du token correspond à l'email de l'utilisateur")
    void generateResetToken_shouldEmbedUserEmailAsSubject() {
        String token = jwtResetTokenService.generateResetToken(user);

        String extractedUsername = jwtResetTokenService.extractUsername(token);

        assertThat(extractedUsername).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("JWT-03 | generateResetToken() - Succès : deux appels successifs génèrent des tokens différents")
    void generateResetToken_shouldGenerateDifferentTokens_onSuccessiveCalls() throws InterruptedException {
        String firstToken = jwtResetTokenService.generateResetToken(user);
        Thread.sleep(1000); // garantit un "issuedAt" différent (résolution à la seconde)
        String secondToken = jwtResetTokenService.generateResetToken(user);

        assertThat(firstToken).isNotEqualTo(secondToken);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  extractUsername()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("JWT-04 | extractUsername() - Succès : extraction correcte depuis un token valide")
    void extractUsername_shouldReturnEmail_whenTokenIsValid() {
        String token = jwtResetTokenService.generateResetToken(user);

        String username = jwtResetTokenService.extractUsername(token);

        assertThat(username).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("JWT-05 | extractUsername() - Échec : token malformé")
    void extractUsername_shouldThrow_whenTokenIsMalformed() {
        assertThatThrownBy(() -> jwtResetTokenService.extractUsername("token-invalide"))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("JWT-06 | extractUsername() - Échec : token signé avec une autre clé")
    void extractUsername_shouldThrow_whenTokenSignedWithDifferentKey() {
        JwtResetTokenService otherService =
                new JwtResetTokenService("fedcba9876543210fedcba9876543210fedcba9876543210");
        String token = otherService.generateResetToken(user);

        assertThatThrownBy(() -> jwtResetTokenService.extractUsername(token))
                .isInstanceOf(JwtException.class);
    }

    @Test
    @DisplayName("JWT-07 | extractUsername() - Échec : token expiré")
    void extractUsername_shouldThrow_whenTokenIsExpired() throws InterruptedException {
        String token = jwtResetTokenService.generateResetToken(user);

        Thread.sleep(61_000);

        assertThatThrownBy(() -> jwtResetTokenService.extractUsername(token))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
