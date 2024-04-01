package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.config.AppUnitTest;
import com.kt.rest.demoEcommerce.model.auth.AuthenticationRequest;
import com.kt.rest.demoEcommerce.model.auth.RegisterRequest;
import com.kt.rest.demoEcommerce.model.entity.Role;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RoleRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@AppUnitTest
class AuthenticationServiceTest {
    RegisterRequest mRegisterRequest;
    AuthenticationRequest mAuthenticationRequest;


    @Mock
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private JwtService jwtService;

//    @InjectMocks // Equivalent to creating an instance with new AuthenticationService() within @BeforeEach setup
    AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, passwordEncoder, roleRepository, jwtService);
    }

    @DisplayName("Authentication Service - test successful registration")
    @Test
    void testRegister_SuccessfulRegistration() {
        mRegisterRequest = new RegisterRequest("testuser", "testemail@example.com", "password");

        when(userRepository.existsByEmailOrUsername(mRegisterRequest.email(), mRegisterRequest.username())).thenReturn(false);
        when(roleRepository.getByName(Role.USER)).thenReturn(new Role(1, Role.USER));
        // What Mockito should when the save() method is called
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            // Retrieve the argument passed to the save() method (i.e, User instance)
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1);
            return savedUser;
        });

        User registeredUser = authenticationService.register(mRegisterRequest);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("testemail@example.com", registeredUser.getEmail());
        assertTrue(passwordEncoder.matches("password", registeredUser.getPassword()));
        assertEquals(1, registeredUser.getId());
    }

    @DisplayName("Authentication Service - test successful login")
    @Test
    void testLogin_ValidCredentials() {
        mAuthenticationRequest = new AuthenticationRequest("testuser", "password");
        User mockUser = User.builder().id(1).username("testuser").password(passwordEncoder.encode("password")).email("testemail@example.com").build();

        when(userRepository.findOneByUsername(mAuthenticationRequest.getUsername())).thenReturn(Optional.of(mockUser));

        User loggedInUser = authenticationService.login(mAuthenticationRequest);

        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.getUsername());
        assertEquals("testemail@example.com", loggedInUser.getEmail());
    }
}