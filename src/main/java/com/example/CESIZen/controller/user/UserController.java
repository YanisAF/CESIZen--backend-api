package com.example.CESIZen.controller.user;

import com.example.CESIZen.assembler.UserModelAssembler;
import com.example.CESIZen.dto.user.DeactivateResponseDto;
import com.example.CESIZen.dto.user.UserDtoRequest;
import com.example.CESIZen.dto.user.UserDtoResponse;
import com.example.CESIZen.dto.user.UserPatchRequest;
import com.example.CESIZen.exception.AllUserException;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.service.user.UserDeactivateService;
import com.example.CESIZen.service.user.UserService;
import com.example.CESIZen.utils.Routes;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserModelAssembler userModelAssembler;
    private final UserDeactivateService userDeactivateService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(Routes.USER_LIST)
    public ResponseEntity<List<EntityModel<UserDtoResponse>>> getAllUsers(){
        List<EntityModel<UserDtoResponse>> usersList = userService.getAllUsers()
                .stream()
                .map(userModelAssembler::toModel)
                .toList();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(usersList);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(Routes.FILTER_USER_ROLE_LIST)
    public ResponseEntity<List<EntityModel<UserDtoResponse>>> getAllUsersWithFilter(){
        List<EntityModel<UserDtoResponse>> usersList = userService.getAllUsersWithUserRole()
                .stream()
                .map(userModelAssembler::toModel)
                .toList();
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(usersList);
    }

    @GetMapping(Routes.USER_PROFIL)
    public ResponseEntity<UserDtoResponse> getUserProfil(@RequestParam("id") Long id) throws AllUserException {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(userService.getUserById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(Routes.CREATE_USER)
    public ResponseEntity<UserDtoResponse> createUserUrl(@RequestBody UserDtoRequest userDtoRequest) throws AllUserException{
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.createUser(userDtoRequest));
    }

    @PatchMapping(Routes.UPDATE_USER_PROFIL)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserDtoResponse> patchOwnProfile(
            @RequestParam Long id,
            @RequestBody UserPatchRequest patchRequest,
            Authentication authentication) throws AllUserException {

        String currentUsername = authentication.getName();
        UserDtoResponse target = userService.getUserById(id);

        if (!target.getUsername().equals(currentUsername)) {
            throw new AllUserException(
                    HttpStatus.FORBIDDEN,
                    LocalDateTime.now(),
                    "Vous ne pouvez pas modifier le profil d'un autre utilisateur",
                    "/api/v1/users/" + id
            );
        }

        UserDtoResponse updated = userService.patchUser(id, patchRequest);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping(Routes.ROLE_ADMIN)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDtoResponse> patchUserByAdmin(
            @RequestParam Long id,
            @RequestBody UserPatchRequest patchRequest) throws AllUserException {

        UserDtoResponse updated = userService.patchUser(id, patchRequest);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping(Routes.DEACTIVATE_PROFIL)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<DeactivateResponseDto> deactivate(@RequestParam Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(userDeactivateService.deactivate(id));
    }

    @PatchMapping(Routes.REACTIVATE_PROFIL)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DeactivateResponseDto> reactivate(@RequestParam Long id) throws ResourceNotFoundException {
        return ResponseEntity.ok(userDeactivateService.reactivate(id));
    }

    @DeleteMapping(Routes.DELETE_USER)
    public ResponseEntity<Void> deleteUserById(@RequestParam ("id") Long id) throws AllUserException {
        userService.deleteUserById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
