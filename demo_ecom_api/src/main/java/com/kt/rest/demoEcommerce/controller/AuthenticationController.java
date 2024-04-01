package com.kt.rest.demoEcommerce.controller;

import com.kt.rest.demoEcommerce.model.auth.*;
import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.dto.CustomErrorResponse;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.service.AuthenticationService;
import com.kt.rest.demoEcommerce.service.JwtService;
import com.kt.rest.demoEcommerce.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        var authUser = authService.register(request);

        var refreshToken = refreshTokenService.createRefreshToken(authUser);
        ResponseCookie refreshCookie = jwtService.generateRefreshCookie(refreshToken);

        var authPayload = authService.createAuthenticationResponseFromUser(authUser);

        var apiResponse = ApiResponse.builder().success(authPayload).build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        var authUser = authService.login(request);

        var refreshToken = refreshTokenService.createRefreshToken(authUser);
        ResponseCookie refreshCookie = jwtService.generateRefreshCookie(refreshToken);

        var authPayload = authService.createAuthenticationResponseFromUser(authUser);

        var apiResponse = ApiResponse.builder().success(authPayload).build();
        log.info("Cookie {}", refreshCookie.toString());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(apiResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest) {

        // Extract refresh token from Cookie
        String refreshTokenString = jwtService.getRefreshTokenFromCookie(httpServletRequest);

        var authPayload = refreshTokenService.doRefreshToken(refreshTokenString);
        var apiResponse = ApiResponse.builder().success(authPayload).build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser(Authentication authentication) {
        String userName = authentication.getName();
        log.info("Authentication {}",userName);
        if (!userName.isBlank()) {
            User user = authService.findByUsername(userName);
            refreshTokenService.deleteRefreshTokenByUser(user);
        }
        ResponseCookie cleanRefreshCookie = jwtService.getCleanRefreshCookie();
        var apiResponse = ApiResponse.builder().success("User has been signed out").build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanRefreshCookie.toString())
                .body(apiResponse);
    }

}
