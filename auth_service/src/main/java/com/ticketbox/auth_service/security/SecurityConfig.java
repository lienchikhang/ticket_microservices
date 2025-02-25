package com.ticketbox.auth_service.security;

import com.ticketbox.auth_service.exceptionHandler.HandlingFilterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtDecoderCustom jwtDecoderCustom;

    private String[] PUBLIC_ENDPOINTS = {
            "/login",
            "/users/create"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //PUBLIC RESOURCES
        http.authorizeHttpRequests(req -> {
            req.requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated();
        });

        http.oauth2ResourceServer((oauth2ResourceServer) ->
                oauth2ResourceServer
                        .jwt((jwt) ->
                                jwt.decoder(jwtDecoderCustom)
                        )
                        .authenticationEntryPoint(new HandlingFilterException())
        );

        //other config
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
