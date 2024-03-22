package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.auth.*;
import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.entity.RefreshToken;
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
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtEncoder jwtEncoder, RefreshTokenService refreshTokenService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmailOrUsername(request.email(), request.username())) {
            throw new IllegalArgumentException("email or username already exists");
        }
        log.info("New user register request with username: {}", request.username());
        User user = mapRegistertoUser(request);
        // Default role for new user is "USER"
        Role role = roleRepository.getByName(Role.USER);
        user.setRoles(Set.of(role));

        var savedUser = userRepository.save(user);
        var authResponse = createAuthenticationResponseFromValidUser(savedUser);
        return authResponse;
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

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

        var authResponse = createAuthenticationResponseFromValidUser(user);
        return authResponse;
    }
    public User mapRegistertoUser(RegisterRequest request) {
        // No Role is set yet
        return User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
    }

    public AuthenticationResponse createAuthenticationResponseFromValidUser(User user) {
        var jwtToken = getToken(user);

        var rToken = refreshTokenService.findByUser(user)
                .orElseGet(() -> refreshTokenService.createRefreshToken(user.getUsername()));

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(rToken.getToken())
                .id(user.getId()).build();
    }

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
    public String getToken(User user) {
//        Instant now = Instant.now();
//        long expiry = 10; // 15 minutes
//
//        String scope = user.getRoles().stream()
//                .map(role -> role.getName())
//                .collect(Collectors.joining(" "));
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("self")
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(expiry))
//                .subject(user.getUsername()) // this is value of authentication.getName()
//                .claim("scope", scope)
//                .build();
//        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return jwtService.getToken(user);
    }
}
