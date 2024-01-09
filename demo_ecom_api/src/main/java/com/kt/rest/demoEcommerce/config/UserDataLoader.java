package com.kt.rest.demoEcommerce.config;

import com.kt.rest.demoEcommerce.model.auth.Role;
import com.kt.rest.demoEcommerce.model.auth.User;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;

@Component
public class UserDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public UserDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> optionalAdmin = userRepository.findByEmail("admin@email.com");
        Optional<User> optionalTestUser = userRepository.findByEmail("test@email.com");
        if (!optionalAdmin.isPresent()) {
            User admin = User.builder()
                    .name("admin")
                    .email("admin@email.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
        if (!optionalTestUser.isPresent()) {
            User testUser = User.builder()
                    .name("Test user")
                    .email("test@email.com")
                    .password(passwordEncoder.encode("test123"))
                    .role(Role.USER)
                    .build();
            userRepository.save(testUser);
        }

       ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("data.sql"));
       DatabasePopulatorUtils.execute(populator, dataSource);
    }
}
