package com.kt.rest.demoEcommerce.model.auth;

import com.kt.rest.demoEcommerce.model.entities.Order;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String email;
    private String name, password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

    public User() {
    }


    public User(Integer id, String email, String name, String password, Role role, List<Token> tokens, List<Order> orders) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.tokens = tokens;
        this.orders = orders;
    }
    public User(String email, String name, String password, Role role, List<Token> tokens, List<Order> orders) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.tokens = tokens;
        this.orders = orders;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public static class UserBuilder {
        private Integer id;
        private String email, name, password;
        private Role role;
        private List<Token> tokens;
        private List<Order> orders;

        private UserBuilder() {
        }


        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder role(Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder tokens(List<Token> tokens) {
            this.tokens = tokens;
            return this;
        }
        public UserBuilder orders(List<Order> orders) {
            this.orders = orders;
            return this;
        }


        public User build() {
            return new User(email, name, password, role, tokens, orders);
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
