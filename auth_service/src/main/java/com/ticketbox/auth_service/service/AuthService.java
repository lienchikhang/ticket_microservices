package com.ticketbox.auth_service.service;

import com.nimbusds.jose.JOSEException;
import com.ticketbox.auth_service.dto.request.LoginReq;
import com.ticketbox.auth_service.dto.response.IntrospectRes;
import com.ticketbox.auth_service.dto.response.LoginRes;

import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public interface AuthService {

    LoginRes login(LoginReq req) throws NoSuchAlgorithmException, ParseException, JOSEException;

    IntrospectRes introspect(String token) throws ParseException, NoSuchAlgorithmException;

    void logout();
}
