package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.dto.RefreshTokenResponseDTO;
import com.kt.rest.demoEcommerce.model.entity.RefreshToken;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
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
                .expiryDate(Instant.now().plusSeconds(60*15)) // 15 minutes
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByUser(User user) {
        return refreshTokenRepository.findByUser(user);
    }

    public Optional<RefreshToken> findByToken(String token) {
        var rToken = refreshTokenRepository.findByToken(token);
        return rToken;
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please login ...");
        }
        return token;
    }
    @Transactional
    public RefreshTokenResponseDTO doRefreshToken(String token) {
        return findByToken(token)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    var jwtToken = jwtService.getToken(user);
//                    var rToken = createRefreshToken(user.getUsername());
                    log.info("User {} refresh token", user.getId());
                    return new RefreshTokenResponseDTO(jwtToken);
                }).orElseThrow(() -> new RuntimeException("Refresh token is not found."));
    }
}
