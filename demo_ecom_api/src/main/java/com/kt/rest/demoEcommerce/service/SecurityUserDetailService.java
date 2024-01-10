package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.security.SecurityUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityUserDetailService implements UserDetailsService {
    public final UserService userService;

    public SecurityUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findUserByUserName(username);
        return new SecurityUserDetail(user);
    }
}
