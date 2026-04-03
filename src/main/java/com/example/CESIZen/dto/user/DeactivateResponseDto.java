package com.example.CESIZen.dto.user;

public record DeactivateResponseDto(
        Long id,
        String message,
        boolean isActive
) {}
