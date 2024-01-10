package com.kt.rest.demoEcommerce.service;

import com.kt.rest.demoEcommerce.exeptions.ResourceNotFoundException;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RoleRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    public final UserRepository userRepository;
    public final RoleRepository roleRepository;

    public User findUserByUserName(String username) {
        return userRepository.findOneByUsername(username).orElseThrow(() ->
                new ResourceNotFoundException("User", "username", username));
    }
}
