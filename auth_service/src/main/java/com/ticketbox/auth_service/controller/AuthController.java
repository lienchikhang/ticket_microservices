package com.ticketbox.auth_service.controller;

import com.nimbusds.jose.JOSEException;
import com.ticketbox.auth_service.dto.request.LoginReq;
import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.dto.response.IntrospectRes;
import com.ticketbox.auth_service.dto.response.LoginRes;
import com.ticketbox.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

@Slf4j
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

    AuthService authService;

    @PostMapping("/login")
    public AppResponse<LoginRes> login(@RequestBody @Valid LoginReq req) throws NoSuchAlgorithmException, ParseException, JOSEException {
        return AppResponse.<LoginRes>builder()
                .code(200)
                .message("Login successfully!")
                .data(authService.login(req))
                .build();
    }

    @GetMapping("/introspect-token")
    public AppResponse<IntrospectRes> introspectToken(@RequestHeader("Authorization") String authentication) throws ParseException, NoSuchAlgorithmException {
        return AppResponse.<IntrospectRes>builder()
                .code(200)
                .data(authService.introspect(authentication.split(" ")[1]))
                .build();
    }

    @PostMapping("/logout")
    public AppResponse<Void> logout(@RequestHeader("Authorization") String authentication) throws ParseException, NoSuchAlgorithmException {
        authService.logout(authentication.split(" ")[1]);
        return AppResponse.<Void>builder()
                .code(200)
                .message("Logout successfully!")
                .build();
    }

    @GetMapping("/test")
//    @PreAuthorize("hasAuthority('EDIT_PROFILE')")
    public AppResponse<String> testAuthorize() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("authentication 3 {}", auth.getPrincipal().toString());
        if (auth.getPrincipal() instanceof Jwt jwt) {
            log.info("Claims: {}", jwt.getClaims());
        }
        auth.getAuthorities().forEach(authority -> log.info("authority {}", authority.toString()));

        return AppResponse.<String>builder()
                .code(200)
                .message("test")
                .build();
    }
}
