package com.example.CESIZen.service.user;

import com.example.CESIZen.dto.user.UserDtoRequest;
import com.example.CESIZen.dto.user.UserDtoResponse;
import com.example.CESIZen.dto.user.UserPatchRequest;
import com.example.CESIZen.enums.Roles;
import com.example.CESIZen.exception.AllUserException;
import com.example.CESIZen.mapper.UserMapper;
import com.example.CESIZen.model.user.User;
import com.example.CESIZen.repository.ResultDiagnosisRepository;
import com.example.CESIZen.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ResultDiagnosisRepository resultDiagnosisRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       ResultDiagnosisRepository resultDiagnosisRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.resultDiagnosisRepository = resultDiagnosisRepository;
    }

    public UserDtoResponse register(UserDtoRequest userDtoRequest) throws AllUserException {
        if (userNameOrEmailIsNotPresent(userDtoRequest) || userRepository.existsByEmail(userDtoRequest.getUserName())){
            throw new AllUserException(
                    HttpStatus.NOT_ACCEPTABLE,
                    LocalDateTime.now(),
                    "Username is blank or already exists",
                    "/api/v1/auth/register"
            );
        }
        User newUser = requestCreateUser(userDtoRequest, false);
        User userSaved = userRepository.save(newUser);
        return UserMapper.toDto(userSaved);
    }

    private User requestCreateUser(UserDtoRequest userDtoRequest, boolean isAdmin) throws AllUserException{
        if (userRepository.existsByEmail(userDtoRequest.getEmail())){
            throw new AllUserException(
                    HttpStatus.NOT_ACCEPTABLE,
                    LocalDateTime.now(),
                    "Email or username already exists",
                    "/api/v1/users/register"
            );
        }
        User newUser = getUser(userDtoRequest);
        if (isAdmin) {
            newUser.setRole(Roles.ROLE_ADMIN);
        } else {
            newUser.setRole(Roles.ROLE_USER);
        }
        return newUser;
    }

    private User getUser(UserDtoRequest userDtoRequest) {
        User newUser = new User();
        newUser.setUserName(userDtoRequest.getUserName());
        newUser.setEmail(userDtoRequest.getEmail());
        newUser.setPhone(userDtoRequest.getPhone());
        newUser.setPassword(passwordEncoder.encode(userDtoRequest.getPassword()));
        return newUser;
    }

    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) throws AllUserException{
        userDtoRequest.setEmail(userDtoRequest.getEmail().toLowerCase());
        userDtoRequest.setUserName(userDtoRequest.getUserName().toLowerCase());
        User newUser = requestCreateUser(userDtoRequest, false);
        User userSaved = userRepository.save(newUser);
        return UserMapper.toDto(userSaved);
    }

    public UserDtoResponse getUserById(Long id) throws AllUserException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AllUserException(
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now(),
                        "User not found",
                        "/api/users/profil"
                ));
        return UserMapper.toDto(user);
    }

    public UserDtoResponse registerAdmin(UserDtoRequest userDtoRequest) throws AllUserException{
        if (userNameOrEmailIsNotPresent(userDtoRequest) || userRepository.existsByEmail(userDtoRequest.getUserName())){
            throw new AllUserException(
                    HttpStatus.NOT_ACCEPTABLE,
                    LocalDateTime.now(),
                    "Username is blank or already exists",
                    "/api/v1/auth/register"
            );
        }
        User newUser = requestCreateUser(userDtoRequest, true);
        User userSaved = userRepository.save(newUser);
        return UserMapper.toDto(userSaved);
    }

    public UserDtoResponse getUserAdminById(Long id) throws AllUserException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AllUserException(
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now(),
                        "User not found",
                        "/api/users/profil"
                ));
        return UserMapper.toDto(user);
    }

    public UserDtoResponse getUserByName(UserDtoRequest userDtoRequest){
        User user = userRepository.findByUserName(userDtoRequest.getUserName());
        return UserMapper.toDto(user);
    }

    private static boolean userNameOrEmailIsNotPresent(UserDtoRequest userDtoRequest) {
        return (userDtoRequest.getUserName().isBlank() || userDtoRequest.getEmail().isBlank() || userDtoRequest.getPassword().isBlank());
    }

    public List<UserDtoResponse> getAllUsers(){
        List<User> usersList = userRepository.findAll();
        return usersList.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public List<UserDtoResponse> getAllUsersWithUserRole(){
        List<User> usersList = userRepository.findAllUserIfUserRole();
        return usersList.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public UserDtoResponse patchUser(Long id, UserPatchRequest patchRequest) throws AllUserException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AllUserException(
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now(),
                        "User not found",
                        "/api/v1/users/" + id
                ));

        if (patchRequest.getUserName() != null && !patchRequest.getUserName().isBlank()) {
            user.setUserName(patchRequest.getUserName());
        }

        if (patchRequest.getEmail() != null && !patchRequest.getEmail().isBlank()) {
            if (userRepository.existsByEmail(patchRequest.getEmail())) {
                throw new AllUserException(
                        HttpStatus.NOT_ACCEPTABLE,
                        LocalDateTime.now(),
                        "Email already exists",
                        "/api/v1/users/" + id
                );
            }
            user.setEmail(patchRequest.getEmail());
        }

        if (patchRequest.getPhone() != null && !patchRequest.getPhone().isBlank()) {
            user.setPhone(patchRequest.getPhone());
        }

        User updatedUser = userRepository.save(user);
        return UserMapper.toDto(updatedUser);
    }

    public void deleteUserById(Long id) throws AllUserException{
        userRepository.findById(id)
                .orElseThrow(() -> new AllUserException(
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now(),
                        "User not found",
                        "/api/users/profil"
                ));
        resultDiagnosisRepository.deleteAllByUserId(id);
        userRepository.deleteResetTokenByUserId(id);
        userRepository.deleteById(id);
    }
}
