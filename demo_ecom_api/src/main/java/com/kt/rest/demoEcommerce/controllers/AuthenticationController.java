package com.kt.rest.demoEcommerce.controllers;

import com.kt.rest.demoEcommerce.models.auth.*;
import com.kt.rest.demoEcommerce.models.dataModels.CustomErrorResponse;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.services.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authService;
    private final UserRepository userRepository;

    public AuthenticationController(AuthenticationService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        return ResponseEntity.ok(authService.register(request));
        try {
            AuthenticationResponse authResponse = authService.register(request);
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(CustomErrorResponse.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .statusText(e.getMessage())
                            .build());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }


}
