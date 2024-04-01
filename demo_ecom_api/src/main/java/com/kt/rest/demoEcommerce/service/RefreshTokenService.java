package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.dto.RefreshTokenResponseDTO;
import com.kt.rest.demoEcommerce.model.entity.RefreshToken;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

    private long refreshCookieExp;
    final RefreshTokenRepository refreshTokenRepository;
    final UserService userService;
    final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserService userService, JwtService jwtService, @Value("${demo.auth.refreshCookieExpSecond}") long refreshCookieExp) {
        this.refreshCookieExp = refreshCookieExp;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userService = userService;
        this.jwtService = jwtService;
        log.info("Refresh Cookie exp: {}", refreshCookieExp);
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {
        // If there's an existing refresh token for current user, delete it and create new one
        var existingRefreshToken = findByUser(user);
        if (existingRefreshToken.isPresent()) {
            this.refreshTokenRepository.delete(existingRefreshToken.get());
        }
        var refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(refreshCookieExp))
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
    public void deleteRefreshTokenByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new IllegalArgumentException(token.getToken() + " Refresh token is expired. Please login ...");
        }
        return token;
    }
    @Transactional
    public RefreshTokenResponseDTO doRefreshToken(String token) {
        log.info("Token {}", token);
        if (token == null || token.length() == 0) {
            throw new NoSuchElementException("Refresh token is empty");
        }
        return findByToken(token)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {

                    var jwtToken = jwtService.getToken(user);
                    log.info("User {} refresh token", user.getId());
                    return new RefreshTokenResponseDTO(jwtToken);
                }).orElseThrow(() -> new NoSuchElementException("Refresh token is not found."));
    }

}
