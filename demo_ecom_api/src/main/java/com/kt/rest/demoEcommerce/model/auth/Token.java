package com.kt.rest.demoEcommerce.model.auth;

import com.kt.rest.demoEcommerce.model.entity.User;
import jakarta.persistence.*;

@Table
@Entity
public class Token {
    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked, expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public Token() {
    }
    public static TokenBuilder builder() {
        return new TokenBuilder();
    }
    public Token(Integer id, String token, TokenType tokenType, boolean revoked, boolean expired, User user) {
        this.id = id;
        this.token = token;
        this.tokenType = tokenType;
        this.revoked = revoked;
        this.expired = expired;
        this.user = user;
    }

    public Token(String token, boolean revoked, boolean expired, User user) {
        this.token = token;
        this.revoked = revoked;
        this.expired = expired;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class TokenBuilder {
        private String token;
        private boolean revoked, expired;
        private User user;
        private TokenBuilder() {

        }
        public TokenBuilder token(String token){
            this.token = token;
            return this;
        }
        public TokenBuilder revoked(boolean revoked) {
            this.revoked = revoked;
            return this;
        }
        public TokenBuilder expired(boolean expired) {
            this.expired = expired;
            return this;
        }
        public TokenBuilder user(User user) {
            this.user = user;
            return this;
        }
        public Token build() {
            return new Token(token, revoked, expired, user);
        }
    }
}
