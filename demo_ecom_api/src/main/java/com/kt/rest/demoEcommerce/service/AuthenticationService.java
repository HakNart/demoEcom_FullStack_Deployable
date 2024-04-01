package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.auth.*;
import com.kt.rest.demoEcommerce.model.entity.Role;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RoleRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmailOrUsername(request.email(), request.username())) {
            throw new IllegalArgumentException("email or username already exists");
        }
        log.info("New user register request with username: {}", request.username());
        User user = mapRegistertoUser(request);
        // Default role for new user is "USER"
        Role role = roleRepository.getByName(Role.USER);
        user.setRoles(Set.of(role));

        var savedUser = userRepository.save(user);
        return savedUser;
    }

    public User login(AuthenticationRequest request) {

        var username = request.getUsername();
        var password = request.getPassword();
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username is required.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("password is required.");
        }

        User user = userRepository
                .findOneByUsername(username)
                        .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                                .orElseThrow(() -> new IllegalArgumentException("invalid username or password"));

        return user;
    }

    public AuthenticationResponse createAuthenticationResponseFromUser(User user) {
        var jwtToken = jwtService.getToken(user);
        var authPayload = AuthenticationResponse.builder()
                .token(jwtToken).id(user.getId()).build();
        return authPayload;
    }

    public User mapRegistertoUser(RegisterRequest request) {
        // No Role is set yet
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }

    public User findByUsername(String username) {
        return userRepository.findOneByUsername(username).orElseThrow(() -> new IllegalArgumentException("invalid username"));
    }

}
