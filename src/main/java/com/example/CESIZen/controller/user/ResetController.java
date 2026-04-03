package com.example.CESIZen.controller.user;

import com.example.CESIZen.dto.reset.JwtDtoResponse;
import com.example.CESIZen.dto.reset.ResetDtoRequest;
import com.example.CESIZen.dto.reset.ResetPasswordRequestDto;
import com.example.CESIZen.dto.reset.VerifyPinRequestDto;
import com.example.CESIZen.exception.ResourceNotFoundException;
import com.example.CESIZen.service.reset.PasswordResetService;
import com.example.CESIZen.utils.Routes;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ResetController {

    private final PasswordResetService passwordResetService;

    public ResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping(Routes.REQUEST_PASSWORD)
    public ResponseEntity<?> requestReset(@Valid @RequestBody ResetDtoRequest request) throws ResourceNotFoundException {
        passwordResetService.requestReset(
                request.getIdentifier(),
                request.getChannel()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping(Routes.VERIFY_PIN)
    public ResponseEntity<?> verifyPin(@Valid @RequestBody VerifyPinRequestDto request) throws ResourceNotFoundException {
        String resetJwt = passwordResetService.verifyPin(
                request.getIdentifier(),
                request.getPin()
        );
        return ResponseEntity.ok(new JwtDtoResponse(resetJwt));
    }

    @PostMapping(Routes.RESET_PASSWORD)
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequestDto request) throws Exception {
        passwordResetService.resetPassword(
                request.getJwt(),
                request.getNewPassword(),
                request.getChannel()
        );
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .build();

    }
}
