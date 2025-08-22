package com.security.oriented.dto;

public record UserResponse(
        Long id,
        String username,
        String email,
        String role) {
}
