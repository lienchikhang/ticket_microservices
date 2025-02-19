package com.ticketbox.auth_service.service;

import com.ticketbox.auth_service.dto.request.LoginReq;
import com.ticketbox.auth_service.dto.response.LoginRes;

public interface AuthService {

    LoginRes login(LoginReq req);

    Boolean introspect();

    void logout();
}
