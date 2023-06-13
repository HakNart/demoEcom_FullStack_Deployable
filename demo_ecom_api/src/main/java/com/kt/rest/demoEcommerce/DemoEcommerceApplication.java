package com.kt.rest.demoEcommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableCaching
public class DemoEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoEcommerceApplication.class, args);
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
					registry.addMapping("/**")
							.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD")
							.allowedOrigins("http://localhost:5173")
							.allowedOrigins("http://127.0.0.1:5173")
							.allowedOrigins("http://localhost:3000")
							.allowedOrigins("http://localhost:3000/")
							.allowedOrigins("http://192.168.32.4:3000")
							.allowedOrigins("http://127.0.0.1:3000")
							.allowedOrigins("https://haknart.github.io")
							.allowedOrigins("*");
				}
			};
		}

}
