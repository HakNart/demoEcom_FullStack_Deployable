package com.kt.rest.demoEcommerce.config;

import com.kt.rest.demoEcommerce.model.entity.Role;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RoleRepository;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Set;

@Profile("dev")
@Component
public class UserDataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final DataSource dataSource;

    public UserDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, DataSource dataSource) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<User> optionalAdmin = userRepository.findByEmail("admin@email.com");
        Optional<User> optionalTestUser = userRepository.findByEmail("test@email.com");
        Optional<Role> optionalRoleAdmin = roleRepository.findByName(Role.ADMIN);
        Optional<Role> optionalRoleUser = roleRepository.findByName(Role.USER);
        Role rAdmin = null;
        Role rUser = null;
        if (!optionalRoleAdmin.isPresent()) {
            rAdmin = new Role(Role.ADMIN);
            roleRepository.save(rAdmin);
        }
        if (!optionalRoleUser.isPresent()) {
            rUser = new Role(Role.USER);
            roleRepository.save(rUser);
        }
        if (!optionalAdmin.isPresent()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@email.com")
                    .password(passwordEncoder.encode("admin"))
                    .roles(Set.of(rAdmin))
                    .build();
            userRepository.save(admin);
        }
        if (!optionalTestUser.isPresent()) {
            User testUser = User.builder()
                    .username("user1")
                    .email("test@email.com")
                    .password(passwordEncoder.encode("test123"))
                    .roles(Set.of(rUser))
                    .build();
            userRepository.save(testUser);
        }
//
       ResourceDatabasePopulator populator = new ResourceDatabasePopulator(new ClassPathResource("data.sql"));
       DatabasePopulatorUtils.execute(populator, dataSource);
    }
}
