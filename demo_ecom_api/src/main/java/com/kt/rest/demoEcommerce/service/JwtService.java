package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.entity.RefreshToken;
import com.kt.rest.demoEcommerce.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {
    final JwtEncoder jwtEncoder;

    private String refreshCookieName;


    public JwtService(JwtEncoder jwtEncoder, @Value("${demo.auth.refreshCookieName}") String refreshCookieName) {
        this.jwtEncoder = jwtEncoder;
        this.refreshCookieName = refreshCookieName;
    }


    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        return getCookieValueByName(request, refreshCookieName);
    }

    public ResponseCookie getCleanRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from(refreshCookieName, "").path("/api/v1/auth/refreshToken").build();
        return cookie;
    }
    public  ResponseCookie generateRefreshCookie(RefreshToken refreshToken) {
        Duration maxAge = Duration.between(Instant.now(), refreshToken.getExpiryDate());
        return generateCookie(refreshCookieName, refreshToken.getToken(), "/api/v1/auth/refreshToken", maxAge);
    }


    public String getToken(User user) {
        Instant now = Instant.now();
        long expiry = 15; // 5 minutes

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

    private ResponseCookie generateCookie(String name, String value, String path, Duration maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value).path(path)
                .maxAge(maxAge).httpOnly(true).build();
        return cookie;
    }

    private String getCookieValueByName(HttpServletRequest request, String name) {
        Cookie cookie = WebUtils.getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }
}
