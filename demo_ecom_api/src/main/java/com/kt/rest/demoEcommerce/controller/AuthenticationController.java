package com.kt.rest.demoEcommerce.controller;

import com.kt.rest.demoEcommerce.model.auth.*;
import com.kt.rest.demoEcommerce.model.dto.ApiResponse;
import com.kt.rest.demoEcommerce.model.dto.CustomErrorResponse;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.service.AuthenticationService;
import com.kt.rest.demoEcommerce.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final AuthenticationService authService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService, UserRepository userRepository) {
        this.authService = authenticationService;
        this.refreshTokenService = refreshTokenService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        var authPayload = authService.register(request);

        var apiResponse = ApiResponse.builder().success(authPayload).build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        var authPayload = authService.login(request);

        var apiResponse = ApiResponse.builder().success(authPayload).build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        var authPayload = refreshTokenService.doRefreshToken(refreshTokenRequestDTO.refreshToken());

        var apiResponse = ApiResponse.builder().success(authPayload).build();
        return ResponseEntity.ok(apiResponse);
    }

}
