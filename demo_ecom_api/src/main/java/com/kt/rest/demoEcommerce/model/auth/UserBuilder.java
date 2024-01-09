package com.kt.rest.demoEcommerce.model.auth;

import com.kt.rest.demoEcommerce.model.entities.Order;

import java.util.List;

public class UserBuilder {
    private Integer id;
    private String email;
    private String name;
    private String password;
    private Role role;
    private List<Token> tokens;
    private List<Order> orders;

    public UserBuilder setId(Integer id) {
        this.id = id;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public UserBuilder setTokens(List<Token> tokens) {
        this.tokens = tokens;
        return this;
    }

    public UserBuilder setOrders(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public User createUser() {
        return new User(id, email, name, password, role, tokens, orders);
    }
}