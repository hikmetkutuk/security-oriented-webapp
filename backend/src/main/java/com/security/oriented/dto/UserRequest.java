package com.security.oriented.dto;

public record UserRequest(
        String username,
        String email,
        String password,
        String roleName) {

}
