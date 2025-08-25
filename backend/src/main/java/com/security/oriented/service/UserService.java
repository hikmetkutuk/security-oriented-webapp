package com.security.oriented.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.security.oriented.dto.UserResponse;
import com.security.oriented.model.User;
import com.security.oriented.model.UserDetail;
import com.security.oriented.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            logger.info("Retrieved {} users from the database", users.size());
            return users.stream()
                    .map(user -> new UserResponse(
                            user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getRole().getRoleName().name()))
                    .toList();
        } catch (Exception e) {
            logger.error("Error retrieving users", e);
            throw new RuntimeException("Failed to retrieve users", e);
        }
    }

    public UserResponse getUserById(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            logger.info("Retrieved user with ID {}", id);
            return new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().getRoleName().name());
        } catch (Exception e) {
            logger.error("Error retrieving user with ID {}", id, e);
            throw new RuntimeException("Failed to retrieve user", e);
        }
    }

    public void updateUserRole(Long userId, String roleName) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.getRole().setRoleName(Enum.valueOf(com.security.oriented.model.AppRole.class, roleName));
            userRepository.save(user);
            logger.info("Updated role for user ID {} to {}", userId, roleName);
        } catch (Exception e) {
            logger.error("Error updating role for user ID {}", userId, e);
            throw new RuntimeException("Failed to update user role", e);
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new UserDetail().build(user);
    }
}
