package com.security.oriented.dto;

public record PostResponse(
        Long id,
        String title,
        String content,
        String author) {

}
