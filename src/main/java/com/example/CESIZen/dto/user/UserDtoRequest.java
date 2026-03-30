package com.example.CESIZen.dto.user;

import com.example.CESIZen.enums.Roles;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 32, message = "Username must be between 3 and 32 characters")
    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("email")
    @NotBlank(message = "Email is required")
    @Size(min = 6, max = 32, message = "Email must be between 6 and 32 characters")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private Roles role;

    public UserDtoRequest(String id, String s, String s1, Roles roles) {
    }
}
