package com.kt.rest.demoEcommerce.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("web.cors")
@Getter
@Setter
public class CorsProperties {
    private List<String> allowedOrigins;
    private List<String> allowedMethods;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }
}
