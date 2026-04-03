package com.example.CESIZen.service;

import com.example.CESIZen.dto.user.UserDtoRequest;
import com.example.CESIZen.dto.user.UserDtoResponse;
import com.example.CESIZen.dto.user.UserPatchRequest;
import com.example.CESIZen.enums.Roles;
import com.example.CESIZen.exception.AllUserException;
import com.example.CESIZen.model.user.User;
import com.example.CESIZen.repository.ResultDiagnosisRepository;
import com.example.CESIZen.repository.UserRepository;
import com.example.CESIZen.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ResultDiagnosisRepository resultDiagnosisRepository;

    @InjectMocks
    private UserService userService;

    // ─── Données de test ───────────────────────────────────────────────────────

    private UserDtoRequest validRequest;
    private User savedUser;

    @BeforeEach
    void setUp() {
        validRequest = new UserDtoRequest();
        validRequest.setUsername("johndoe");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setPhone("0612345678");
        validRequest.setPassword("password123");

        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("johndoe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setPhone("0612345678");
        savedUser.setPassword("$2a$10$hashedpassword");
        savedUser.setRole(Roles.ROLE_USER);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  register()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("USR-01 | register() - Succès : nouvel utilisateur valide")
    void register_shouldReturnUserDto_whenRequestIsValid() throws AllUserException {
        when(userRepository.existsByEmail(validRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDtoResponse result = userService.register(validRequest);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("johndoe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getRole()).isEqualTo(Roles.ROLE_USER);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("USR-02 | register() - Échec : username vide")
    void register_shouldThrow_whenUserNameIsBlank() {
        validRequest.setUsername("   ");

        AllUserException ex = catchThrowableOfType(
                () -> userService.register(validRequest),
                AllUserException.class
        );

        assertThat(ex).isNotNull();
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertThat(ex.getMessage()).contains("blank or already exists");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("USR-03 | register() - Échec : email vide")
    void register_shouldThrow_whenEmailIsBlank() {
        validRequest.setEmail("");

        AllUserException ex = catchThrowableOfType(
                () -> userService.register(validRequest),
                AllUserException.class
        );

        assertThat(ex).isNotNull();
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    @DisplayName("USR-04 | register() - Échec : password vide")
    void register_shouldThrow_whenPasswordIsBlank() {
        validRequest.setPassword("  ");

        AllUserException ex = catchThrowableOfType(
                () -> userService.register(validRequest),
                AllUserException.class
        );

        assertThat(ex).isNotNull();
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("USR-05 | register() - Échec : email déjà existant (vérification via username lookup)")
    void register_shouldThrow_whenUserNameAlreadyExists() {
        when(userRepository.existsByEmail(validRequest.getUsername())).thenReturn(true);

        AllUserException ex = catchThrowableOfType(
                () -> userService.register(validRequest),
                AllUserException.class
        );

        assertThat(ex).isNotNull();
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("USR-06 | register() - Échec : email déjà utilisé par un autre compte")
    void register_shouldThrow_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(validRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(true);

        AllUserException ex = catchThrowableOfType(
                () -> userService.register(validRequest),
                AllUserException.class
        );

        assertThat(ex).isNotNull();
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        verify(userRepository, never()).save(any());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  registerAdmin()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("USR-07 | registerAdmin() - Succès : rôle ADMIN attribué")
    void registerAdmin_shouldAssignAdminRole() throws AllUserException {
        savedUser.setRole(Roles.ROLE_ADMIN);
        when(userRepository.existsByEmail(validRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(validRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashedpassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDtoResponse result = userService.registerAdmin(validRequest);

        assertThat(result.getRole()).isEqualTo(Roles.ROLE_ADMIN);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getUserById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("USR-08 | getUserById() - Succès : utilisateur trouvé")
    void getUserById_shouldReturnUser_whenFound() throws AllUserException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        UserDtoResponse result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("USR-09 | getUserById() - Échec : utilisateur introuvable")
    void getUserById_shouldThrow_whenNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        AllUserException ex = catchThrowableOfType(
                () -> userService.getUserById(99L),
                AllUserException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getMessage()).containsIgnoringCase("not found");
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getAllUsers()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("USR-10 | getAllUsers() - Retourne liste complète")
    void getAllUsers_shouldReturnAllUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("janedoe");
        user2.setEmail("jane@example.com");
        user2.setRole(Roles.ROLE_USER);

        when(userRepository.findAll()).thenReturn(List.of(savedUser, user2));

        List<UserDtoResponse> result = userService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(UserDtoResponse::getUsername)
                .containsExactlyInAnyOrder("johndoe", "janedoe");
    }

    @Test
    @DisplayName("USR-11 | getAllUsers() - Liste vide si aucun utilisateur")
    void getAllUsers_shouldReturnEmptyList_whenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserDtoResponse> result = userService.getAllUsers();

        assertThat(result).isEmpty();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  patchUser()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("USR-12 | patchUser() - Succès : mise à jour du username")
    void patchUser_shouldUpdateUserName() throws AllUserException {
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setUsername("newusername");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("newusername");
        updatedUser.setEmail("john.doe@example.com");
        updatedUser.setRole(Roles.ROLE_USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDtoResponse result = userService.patchUser(1L, patchRequest);

        assertThat(result.getUsername()).isEqualTo("newusername");
    }

    @Test
    @DisplayName("USR-13 | patchUser() - Succès : mise à jour de l'email")
    void patchUser_shouldUpdateEmail() throws AllUserException {
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setEmail("newemail@example.com");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername("johndoe");
        updatedUser.setEmail("newemail@example.com");
        updatedUser.setRole(Roles.ROLE_USER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userRepository.existsByEmail("newemail@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDtoResponse result = userService.patchUser(1L, patchRequest);

        assertThat(result.getEmail()).isEqualTo("newemail@example.com");
    }

    @Test
    @DisplayName("USR-14 | patchUser() - Échec : email déjà utilisé")
    void patchUser_shouldThrow_whenEmailAlreadyTaken() {
        UserPatchRequest patchRequest = new UserPatchRequest();
        patchRequest.setEmail("taken@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        AllUserException ex = catchThrowableOfType(
                () -> userService.patchUser(1L, patchRequest),
                AllUserException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE);
        assertThat(ex.getMessage()).containsIgnoringCase("Email already exists");
    }

    @Test
    @DisplayName("USR-15 | patchUser() - Échec : utilisateur introuvable")
    void patchUser_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        AllUserException ex = catchThrowableOfType(
                () -> userService.patchUser(99L, new UserPatchRequest()),
                AllUserException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  deleteUserById()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("USR-16 | deleteUserById() - Succès : suppression effective")
    void deleteUserById_shouldDeleteUser_whenFound() throws AllUserException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        doNothing().when(resultDiagnosisRepository).deleteAllByUserId(1L);
        doNothing().when(userRepository).deleteResetTokenByUserId(1L);
        doNothing().when(userRepository).deleteById(1L);

        assertThatNoException().isThrownBy(() -> userService.deleteUserById(1L));

        verify(userRepository).deleteResetTokenByUserId(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("USR-17 | deleteUserById() - Échec : utilisateur introuvable")
    void deleteUserById_shouldThrow_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        AllUserException ex = catchThrowableOfType(
                () -> userService.deleteUserById(99L),
                AllUserException.class
        );

        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userRepository, never()).deleteById(any());
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  getAllUsersWithUserRole()
    // ══════════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("USR-18 | getAllUsersWithUserRole() - Retourne uniquement les ROLE_USER")
    void getAllUsersWithUserRole_shouldReturnOnlyUsers() {
        when(userRepository.findAllUserIfUserRole()).thenReturn(List.of(savedUser));

        List<UserDtoResponse> result = userService.getAllUsersWithUserRole();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRole()).isEqualTo(Roles.ROLE_USER);
    }
}
