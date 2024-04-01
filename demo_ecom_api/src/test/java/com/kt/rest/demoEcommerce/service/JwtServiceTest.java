package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.config.AppUnitTest;
import com.kt.rest.demoEcommerce.model.entity.RefreshToken;
import com.kt.rest.demoEcommerce.model.entity.Role;
import com.kt.rest.demoEcommerce.model.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@AppUnitTest
class JwtServiceTest {
    @Autowired private JwtEncoder jwtEncoder;
    @Autowired JwtDecoder jwtDecoder;

    JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(jwtEncoder, "refreshToken");
    }

    @Test
    void testGetRefreshTokenFromCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie cookie = new Cookie("refreshToken", "mockRefreshToken");
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});

        String refreshToken = jwtService.getRefreshTokenFromCookie(request);

        assertEquals("mockRefreshToken", refreshToken);
    }

    @Test
    void testGetCleanRefreshCookie() {
        ResponseCookie cleanCookie = jwtService.getCleanRefreshCookie();

        assertNotNull(cleanCookie);
        assertEquals("refreshToken", cleanCookie.getName());
        assertEquals("",cleanCookie.getValue());
        assertEquals("/api/v1/auth/refreshToken", cleanCookie.getPath());
    }

    @Test
    void testGenerateRefreshCookie() {
        RefreshToken refreshToken = new RefreshToken(1L, "mockToken", Instant.now().plusSeconds(3600), null);
        ResponseCookie generatedCookie = jwtService.generateRefreshCookie(refreshToken);
        System.out.println(generatedCookie.getMaxAge().getSeconds());

        assertNotNull(generatedCookie);
        assertEquals("refreshToken", generatedCookie.getName());
        assertEquals("mockToken", generatedCookie.getValue());
        assertEquals("/api/v1/auth/refreshToken", generatedCookie.getPath());
        assertTrue(generatedCookie.getMaxAge().getSeconds() > 0); // Check if maxAge is set
    }

    @Test
    void testGetToken() {
        User user = User.builder().id(1).username("testuser").roles(Set.of(new Role(Role.USER))).build();

        String token = jwtService.getToken(user);

        assertNotNull(token);
        assertEquals(user.getUsername(), jwtDecoder.decode(token).getSubject());
        assertEquals(user.getRoles().stream().findFirst().get().getName(), jwtDecoder.decode(token).getClaimAsString("scope"));
    }

}