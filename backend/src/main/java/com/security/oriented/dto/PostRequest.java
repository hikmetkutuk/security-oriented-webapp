package com.security.oriented.dto;

public record PostRequest(
        String title,
        String content,
        String author) {

}
