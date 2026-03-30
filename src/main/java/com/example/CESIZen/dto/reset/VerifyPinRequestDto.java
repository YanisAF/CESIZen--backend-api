package com.example.CESIZen.dto.reset;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPinRequestDto {

    @NotBlank
    private String identifier;
    @NotBlank
    private String pin;
}
