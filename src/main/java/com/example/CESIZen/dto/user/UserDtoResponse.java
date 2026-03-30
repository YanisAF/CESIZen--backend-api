package com.example.CESIZen.dto.user;

import com.example.CESIZen.enums.Roles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoResponse {
    private Long id;
    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("email")
    private String email;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("role")
    private Roles role;
    @JsonProperty("last_activity_at")
    private LocalDateTime lastActivityAt = LocalDateTime.now();
}
