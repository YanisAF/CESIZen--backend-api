package com.example.CESIZen.service.authentication;

import com.example.CESIZen.configuration.JwtUtils;
import com.example.CESIZen.dto.login.LoginDtoRequest;
import com.example.CESIZen.exception.AllUserException;
import com.example.CESIZen.model.user.User;
import com.example.CESIZen.repository.UserRepository;
import com.example.CESIZen.service.event.EventService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class AuthService {

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final EventService eventService;


    public AuthService(JwtUtils jwtUtils, AuthenticationManager authenticationManager,
                       UserRepository userRepository, EventService eventService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.eventService = eventService;
    }

    public Map<String, Object> loginManager(LoginDtoRequest loginDtoRequest, HttpServletRequest request) throws AllUserException{
        try{
            loginDtoRequest.setIdentifier(
                    loginDtoRequest.getIdentifier() != null
                            ? loginDtoRequest.getIdentifier().toLowerCase()
                            : null
            );
            Map<String, Object> user = getLoginMap(loginDtoRequest, request);
            if (user != null) return user;
        } catch (AuthenticationException e) {
            throw new AllUserException(
                    HttpStatus.UNAUTHORIZED,
                    LocalDateTime.now(),
                    "Invalid username or password",
                    "/api/v1/auth/login"
            );
        }
        return Map.of();
    }

    private Map<String, Object> getObjectMap(User user, UserDetails userDetails) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> userDto = new HashMap<>();

        userDto.put("id", user.getId());
        userDto.put("user_name", user.getUserName());
        userDto.put("email", user.getEmail());
        userDto.put("role", user.getRole());
        userDto.put("last_activity_at", user.getLastActivityAt());
        response.put("user", userDto);
        response.put("type", "Bearer");
        response.put("token", jwtUtils.generateToken(userDetails));
        return response;
    }

    private Map<String, Object> getLoginMap(LoginDtoRequest loginDtoRequest, HttpServletRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDtoRequest.getIdentifier(),
                        loginDtoRequest.getPassword()));
        if (authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUserName(userDetails.getUsername());
            user.setLastActivityAt(LocalDateTime.now());
            userRepository.save(user);

            getUserInfo(request, user);
            return getObjectMap(user, userDetails);
        }
        return null;
    }

    private Map<String, Object> getMapRegister(LoginDtoRequest loginDtoRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDtoRequest.getIdentifier(),
                        loginDtoRequest.getPassword()));
        if (authentication.isAuthenticated()){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUserName(userDetails.getUsername());
            user.setLastActivityAt(LocalDateTime.now());
            userRepository.save(user);

            eventService.publisherRegister(user, "EMAIL");
            return getObjectMap(user, userDetails);
        }
        return null;
    }

    private void getUserInfo(HttpServletRequest request, User user) {
        String ip = getClientIp(request);
        String device = getDevice(request);
        eventService.publisherConfirmationAuth(user, "EMAIL", ip, device);
    }

    private String getClientIp(HttpServletRequest request) {
        return request.getRemoteAddr();
    }

    private String getDevice(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    public Map<String, Object> registerManager(LoginDtoRequest loginDtoRequest) throws AllUserException{
        try{
            loginDtoRequest.setIdentifier(
                    loginDtoRequest.getIdentifier() != null
                            ? loginDtoRequest.getIdentifier().toLowerCase()
                            : null
            );
            Map<String, Object> user = getMapRegister(loginDtoRequest);
            if (user != null) return user;
        } catch (AuthenticationException e) {
            throw new AllUserException(
                    HttpStatus.UNAUTHORIZED,
                    LocalDateTime.now(),
                    "Invalid user argument",
                    "/api/v1/auth/register"
            );
        }

        return Map.of();
    }

}
