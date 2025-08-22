package com.security.oriented.mapper;

import com.security.oriented.dto.UserRequest;
import com.security.oriented.dto.UserResponse;
import com.security.oriented.model.User;

public class UserMapper {
    public static User toUser(UserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        return user;
    }

    public static UserResponse fromUser(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getRoleName().name());
    }
}
