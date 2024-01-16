package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.auth.*;
import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.entity.Role;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RoleRepository;
import com.kt.rest.demoEcommerce.repository.TokenRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.security.SecurityUserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtEncoder jwtEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtEncoder jwtEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtEncoder = jwtEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
//            request.
//        }
        if (userRepository.existsByEmailOrUsername(request.email(), request.username())) {
            throw new IllegalArgumentException("email or username already exists");
        }
        log.info("New user register request with username: {}", request.username());
        User user = mapRegistertoUser(request);
        // Default role for new user is "USER"
        Role role = roleRepository.getByName(Role.USER);
        user.setRoles(Set.of(role));

        var savedUser = userRepository.save(user);
//        var jwtToken = jwtService.generateToken(new SecurityUserDetail(user));
        var jwtToken = getToken(user);
//        saveUserToken(savedUser, jwtToken);AuthenticationResponse.builder().token(jwtToken).id(savedUser.getId()).build();
        return AuthenticationResponse.builder().token(jwtToken).id(savedUser.getId()).build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        // Authenticate user
//        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(
//                request.getEmail(),
//                request.getPassword()
//        ));
//        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
//        var jwtToken = jwtService.generateToken(user);
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
        var jwtToken = getToken(user);
        // Remove existing token of the user, the issue a new one
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .build();
    }
    private User mapRegistertoUser(RegisterRequest request) {
        // No Role is set yet
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }

//    @Async
//    public CompletableFuture<User> findUser(String email) {
//        Optional<User> user = userRepository.findByEmail(email);
//        if (user.isPresent()) {
//            return CompletableFuture.completedFuture(user.get());
//        } else {
//            return CompletableFuture.failedFuture( new ResourceNotFoundException("User not found"));
//        }
//    }

    // Remove all the tokens associated with the user
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return ;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        Token token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
    private String getToken(User user) {
        Instant now = Instant.now();
        long expiry = 60*15; // 15 minutes

        String scope = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getUsername()) // this is value of authentication.getName()
                .claim("scope", scope)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
