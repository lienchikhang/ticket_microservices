package com.ticketbox.auth_service.exceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.enums.ErrorEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class HandlingFilterException implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //init
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorEnum errorEnum;

        try {

            if (authException.getClass().getSimpleName().equals("InsufficientAuthenticationException"))
                errorEnum = ErrorEnum.INVALID_TOKEN;
            else
                errorEnum = ErrorEnum.valueOf(authException.getMessage());

            //create own custom response
            AppResponse appResponse = AppResponse.builder()
                    .code(errorEnum.getCode())
                    .statusCode(errorEnum.getStatusCode())
                    .message(errorEnum.getMessage())
                    .build();

            //config response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(appResponse));

            //return
            response.flushBuffer();
        } catch (Exception e) {
            log.error("Error in handling authentication exception", e.getMessage());
        }
    }
}
