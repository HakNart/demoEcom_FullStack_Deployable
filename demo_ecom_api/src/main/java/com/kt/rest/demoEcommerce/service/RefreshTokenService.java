package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.auth.AuthenticationResponse;
import com.kt.rest.demoEcommerce.model.entity.RefreshToken;
import com.kt.rest.demoEcommerce.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    final RefreshTokenRepository refreshTokenRepository;
    final UserService userService;
    final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService, JwtService jwtService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;

        this.jwtService = jwtService;
    }

    public RefreshToken createRefreshToken(String username) {
        var refreshToken = RefreshToken.builder()
                .user(userService.findUserByUserName(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(60 * 10)) // 10 minutes
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please login ...");
        }
        return token;
    }

    public AuthenticationResponse doRefreshToken(String token) {
        return findByToken(token)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    var jwtToken = jwtService.getToken(user);
                    var rToken = createRefreshToken(user.getUsername());
                    return AuthenticationResponse.builder()
                            .token(jwtToken)
                            .refreshToken(rToken.getToken())
                            .id(user.getId()).build();
                }).orElseThrow(() -> new RuntimeException("Refresh token is not found."));
    }
}
