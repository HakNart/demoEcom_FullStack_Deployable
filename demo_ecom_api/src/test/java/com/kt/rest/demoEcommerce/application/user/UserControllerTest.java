package com.kt.rest.demoEcommerce.application.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.rest.demoEcommerce.controller.AuthenticationController;
import com.kt.rest.demoEcommerce.model.auth.AuthenticationResponse;
import com.kt.rest.demoEcommerce.model.auth.RegisterRequest;
import com.kt.rest.demoEcommerce.model.entity.RefreshToken;
import com.kt.rest.demoEcommerce.model.entity.User;
import com.kt.rest.demoEcommerce.service.*;
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
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@IntegrationTest
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest({ AuthenticationController.class})
@Slf4j
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;

    @DisplayName("provides membership registration API.")
    @MethodSource("validUserRegistration")
    @ParameterizedTest
    void whenValidRegistration_thenReturnAuthResponse(RegisterRequest registerRequest) throws Exception {
        // Mock authuser
        User mockUser = User.builder().id(1).username("testuser").email("testuser@email.com").password("123").build();
        // Mock refreshToken
        RefreshToken mockRefreshToken = RefreshToken.builder().id(1L).token("testRefreshToken").user(mockUser).build();
        // Mock refresh cookie
        ResponseCookie mRefreshCookie = ResponseCookie.from("refreshToken", mockRefreshToken.getToken())
                .httpOnly(true)
                .secure(false)
                .maxAge(600)
                .path("/")
                .build();

        /*Since these are mock service, any method call would return null. Therefore, we create mock return*/
        // given
        when(authenticationService.register(registerRequest)).thenReturn(mockUser);
        when(refreshTokenService.createRefreshToken(mockUser)).thenReturn(mockRefreshToken);
        when(jwtService.generateRefreshCookie(mockRefreshToken)).thenReturn(mRefreshCookie);
        when(authenticationService.createAuthenticationResponseFromUser(mockUser)).thenReturn(any(AuthenticationResponse.class));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/register")
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

        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/register")
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
