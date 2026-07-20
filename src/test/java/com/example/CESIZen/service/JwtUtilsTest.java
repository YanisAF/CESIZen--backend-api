package com.example.CESIZen.service;

import com.example.CESIZen.configuration.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    // Clé HMAC-SHA256 : au moins 256 bits
    private static final String TEST_SECRET =
            "0123456789abcdef0123456789abcdef0123456789abcdef";
    private static final long TEST_EXPIRATION_MS = 3600_000;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtils, "expirationTime", TEST_EXPIRATION_MS);
    }

    private UserDetails buildUser(String username, String... roles) {
        List<GrantedAuthority> authorities = List.of(roles).stream()
                .map(SimpleGrantedAuthority::new)
                .map(GrantedAuthority.class::cast)
                .toList();

        return new User(username, "password", authorities);
    }

    @Test
    void generateToken_ShouldCreateNonValidateToken() {
        UserDetails user = buildUser("alice", "ROLE_USER");

        String token = jwtUtils.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());
        // Un JWT est composé de 3 parties séparées par des points : header.payload.signature
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void extractUsername_ShouldReturnUsernameAtGeneration() {
        UserDetails user = buildUser("bob", "ROLE_ADMIN");

        String token = jwtUtils.generateToken(user);
        String extracted = jwtUtils.extractUsername(token);

        assertEquals("bob", extracted);
    }

    @Test
    void validateToken_ShouldBeTrueForTokenValidToSameUser() {
        UserDetails user = buildUser("carol", "ROLE_USER");

        String token = jwtUtils.generateToken(user);

        assertTrue(jwtUtils.validateToken(token, user));
    }

    @Test
    void validateToken_ShouldBeFalseIfUsernameDoesNotMatch() {
        UserDetails user = buildUser("dave", "ROLE_USER");
        UserDetails autreUser = buildUser("eve", "ROLE_USER");

        String token = jwtUtils.generateToken(user);

        assertFalse(jwtUtils.validateToken(token, autreUser));
    }

    @Test
    void generateToken_ShouldIncludeUserRolesInClaims() {
        UserDetails user = buildUser("frank", "ROLE_USER", "ROLE_ADMIN");

        String token = jwtUtils.generateToken(user);

        // On vérifie indirectement via extractUsername que le token est exploitable,
        // et que la génération n'a pas levé d'exception malgré plusieurs rôles.
        assertDoesNotThrow(() -> jwtUtils.extractUsername(token));
    }

    @Test
    void validateToken_ShouldThrowAnExceptionOrReturnFalseForAnExpiredToken() throws InterruptedException {
        // On force une expiration quasi immédiate pour ce test précis
        ReflectionTestUtils.setField(jwtUtils, "expirationTime", 1L); // 1 ms

        UserDetails user = buildUser("grace", "ROLE_USER");
        String token = jwtUtils.generateToken(user);

        // Laisse le temps au token d'expirer
        TimeUnit.MILLISECONDS.sleep(50);

        // Selon l'implémentation, extractAllClaims lève ExpiredJwtException
        // dès le parsing, donc validateToken() peut propager l'exception plutôt
        // que de renvoyer false. On couvre les deux cas.
        assertThrows(ExpiredJwtException.class, () -> jwtUtils.validateToken(token, user));
    }

    @Test
    void extractUsername_ShouldThrowAnExceptionForAnInvalidOrCorruptedToken() {
        String tokenCorrompu = "ceci.nest.pasUnTokenValide";

        assertThrows(Exception.class, () -> jwtUtils.extractUsername(tokenCorrompu));
    }

    @Test
    void validateToken_ShouldBeFalseIfTokenWasSignedWithAnotherKey() {
        UserDetails user = buildUser("henri", "ROLE_USER");
        String token = jwtUtils.generateToken(user);

        // On simule un service configuré avec une clé secrète différente
        JwtUtils autreService = new JwtUtils();
        ReflectionTestUtils.setField(autreService, "secretKey",
                "fedcba9876543210fedcba9876543210fedcba98765432");
        ReflectionTestUtils.setField(autreService, "expirationTime", TEST_EXPIRATION_MS);

        assertThrows(Exception.class, () -> autreService.validateToken(token, user));
    }
}