package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.dto.request.LoginReq;
import com.ticketbox.auth_service.dto.response.LoginRes;
import com.ticketbox.auth_service.entity.User;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.UserMapper;
import com.ticketbox.auth_service.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    //mappers
    UserMapper userMapper;

    //structs

    //others
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public LoginRes login(LoginReq req) {

        //CHECKING: user exist
        User existedUser = userMapper.getUserByEmail(req.getEmail())
                .orElseThrow(() -> new AppException(ErrorEnum.INVALID_INFORMATION));

        //CHECKING: password
        if (!passwordEncoder.matches(req.getPassword(), existedUser.getPassword()))
            throw new AppException(ErrorEnum.INVALID_INFORMATION);

        //create tokens

        return null;
    }

    @Override
    public Boolean introspect() {
        return null;
    }

    @Override
    public void logout() {

    }
}
