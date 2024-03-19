package com.kt.rest.demoEcommerce.model.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @JsonProperty("accessToken")
    private String token;
    @JsonProperty("refreshToken")
    private String refreshToken;
    private Integer id;

}
