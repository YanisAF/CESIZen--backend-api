package com.example.CESIZen.controller.authentication;

import com.example.CESIZen.dto.login.LoginDtoRequest;
import com.example.CESIZen.dto.user.UserDtoRequest;
import com.example.CESIZen.exception.AllUserException;
import com.example.CESIZen.mapper.LoginMapper;
import com.example.CESIZen.service.authentication.AuthService;
import com.example.CESIZen.service.user.UserService;
import com.example.CESIZen.utils.Routes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping(Routes.REGISTER)
    public ResponseEntity<?> register(@Valid @RequestBody UserDtoRequest userDtoRequest) throws AllUserException {
        userService.createUser(userDtoRequest);
        LoginDtoRequest loginDtoRequest = LoginMapper.toDtoLogin(userDtoRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.registerManager(loginDtoRequest));
    }

    @PostMapping(Routes.REGISTER_ADMIN)
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody UserDtoRequest userDtoRequest, boolean isAdmin) throws AllUserException{
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.registerAdmin(userDtoRequest));
    }

    @PostMapping(Routes.LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginDtoRequest loginDtoRequest,
                                   HttpServletRequest request) throws AllUserException{
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(authService.loginManager(loginDtoRequest, request));
    }

    @GetMapping(Routes.BACKEND_CHECK)
    public ResponseEntity<?> testBackendUp(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
