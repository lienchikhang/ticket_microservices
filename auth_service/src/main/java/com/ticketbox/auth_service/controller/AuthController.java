package com.ticketbox.auth_service.controller;

import com.nimbusds.jose.JOSEException;
import com.ticketbox.auth_service.dto.request.LoginReq;
import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.dto.response.LoginRes;
import com.ticketbox.auth_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
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

}
