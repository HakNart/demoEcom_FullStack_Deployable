package com.kt.rest.demoEcommerce.application.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.rest.demoEcommerce.controller.AuthenticationController;
import com.kt.rest.demoEcommerce.controller.UserController;
import com.kt.rest.demoEcommerce.model.auth.AuthenticationResponse;
import com.kt.rest.demoEcommerce.model.auth.RegisterRequest;
import com.kt.rest.demoEcommerce.repository.UserRepository;
import com.kt.rest.demoEcommerce.service.AuthenticationService;
import com.kt.rest.demoEcommerce.service.OrderService;
import com.kt.rest.demoEcommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@IntegrationTest
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest({UserController.class, AuthenticationController.class})
@Slf4j
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;

    @DisplayName("provides membership registration API.")
    @MethodSource("validUserRegistration")
    @ParameterizedTest
    void whenValidRegistration_thenReturnAuthResponse(RegisterRequest registerRequest) throws Exception {
        // given
        when(authenticationService.login(any())).thenReturn(any(AuthenticationResponse.class));

        // when
        ResultActions resultActions = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("success"));
    }

    @ParameterizedTest
    @MethodSource("validUserRegistration")
    void whenDuplicatedRegistration_thenReturnException(RegisterRequest registerRequest) throws Exception {
        // when
        when(authenticationService.register(any())).thenThrow(new IllegalArgumentException("email or username already exists"));

        ResultActions resultActions = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)));

        // then
        resultActions
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message", Matchers.notNullValue()));
    }

    public static Stream<Arguments> validUserRegistration() {
        return Stream.of(
                Arguments.of(new RegisterRequest("testUser1", "testUser1@email.com", "password")),
                Arguments.of(new RegisterRequest("testUser2", "testUser2@email.com", "password")),
                Arguments.of(new RegisterRequest("testUser3", "testUser3@email.com", "password"))
        );
    }

    public static Stream<Arguments> invalidUserRegistration() {
        return Stream.of(
                Arguments.of(new RegisterRequest("invalidUser", "invalidTest1@email.com", null)),
                Arguments.of(new RegisterRequest("invalidUser2", "", null)),
                Arguments.of(new RegisterRequest(null, null, null))
        );
    }

}
