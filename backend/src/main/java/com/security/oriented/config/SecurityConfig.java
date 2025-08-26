package com.security.oriented.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.security.oriented.model.AppRole;
import com.security.oriented.model.Role;
import com.security.oriented.model.User;
import com.security.oriented.repository.RoleRepository;
import com.security.oriented.repository.UserRepository;

import static org.springframework.security.config.Customizer.withDefaults;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((req) ->
                req.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository) {
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new IllegalStateException("ROLE_USER not found in database"));

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseThrow(() -> new IllegalStateException("ROLE_ADMIN not found in database"));

            createUserIfNotExists(
                    userRepository,
                    "user1",
                    "user1@example.com",
                    "{noop}user1Pass",
                    false,
                    userRole
            );

            createUserIfNotExists(
                    userRepository,
                    "admin",
                    "admin@example.com",
                    "{noop}adminPass",
                    true,
                    adminRole
            );
        };
    }

    private void createUserIfNotExists(UserRepository userRepository,
                                       String username,
                                       String email,
                                       String password,
                                       boolean accountNonLocked,
                                       Role role) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setAccountNonLocked(accountNonLocked);
            user.setAccountNonExpired(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(true);
            user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
            user.setAccountExpiryDate(LocalDate.now().plusYears(1));
            user.setTwoFactorEnabled(false);
            user.setSignUpMethod("email");
            user.setRole(role);

            userRepository.save(user);
        }
    }
}
