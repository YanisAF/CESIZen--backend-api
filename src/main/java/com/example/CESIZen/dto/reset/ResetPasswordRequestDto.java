package com.example.CESIZen.dto.reset;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDto {

    @NotBlank
    private String jwt;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String channel;
}
