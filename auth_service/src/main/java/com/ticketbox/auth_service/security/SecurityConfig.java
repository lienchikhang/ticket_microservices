package com.ticketbox.auth_service.security;

import com.ticketbox.auth_service.exceptionHandler.HandlingFilterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    JwtDecoderCustom jwtDecoderCustom;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //PUBLIC RESOURCES
        http.authorizeHttpRequests(req -> {
            req.anyRequest().permitAll();
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
