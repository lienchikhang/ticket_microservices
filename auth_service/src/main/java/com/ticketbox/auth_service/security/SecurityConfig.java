package com.ticketbox.auth_service.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //PUBLIC RESOURCES
        http.authorizeHttpRequests(req -> {
            req.anyRequest().permitAll();
        });

        //other config
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
