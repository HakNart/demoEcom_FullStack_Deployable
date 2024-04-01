package com.kt.rest.demoEcommerce.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.rest.demoEcommerce.config.IntegrationTest;
import com.kt.rest.demoEcommerce.model.auth.RegisterRequest;
import com.kt.rest.demoEcommerce.model.entity.Role;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.repository.RoleRepository;
import com.kt.rest.demoEcommerce.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*Integration tests only run when profile is set to test
* For running test on CI server
* */

@EnabledIfEnvironmentVariable(named = "SPRING_PROFILES_ACTIVE", matches = "test")
@Slf4j
@IntegrationTest
@DisplayName("The User APIs")
public class UserIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Value("${spring.profiles.active}")
    String activeProfile;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Provides valid registration")
    void register() throws Exception {
        // give
        // - register request
        log.info("Spring profiles active = " + activeProfile);
        RegisterRequest registerRequest = new RegisterRequest("testUser", "testUser@email.com", "password");
        User user = authenticationService.mapRegistertoUser(registerRequest);
        user.setRoles(Set.of(roleRepository.getByName(Role.USER)));

        // Remove since this test would fail due to timestamp difference between this function all and the 'post' action
//        String jwtToken = authenticationService.getToken(user);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").exists())
                .andExpect(jsonPath("$.payload.accessToken").isNotEmpty());
    }

    @Test
    @DisplayName("Role User and Admin exists")
    void testUserAndAdminRolesExist() throws Exception {
        // DataLoader ran to create 2 default users with user and admin roles
        assertThat(roleRepository.existsByName(Role.USER)).isTrue();
        assertThat(roleRepository.existsByName(Role.ADMIN)).isTrue();
    }
}
