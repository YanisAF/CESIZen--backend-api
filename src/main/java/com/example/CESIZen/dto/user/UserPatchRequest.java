package com.example.CESIZen.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPatchRequest {
    @JsonProperty("user_name")
    private String userName;
    private String email;
    private String phone;
}
