package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.dto.request.UserCreateReq;
import com.ticketbox.auth_service.dto.request.UserUpdateReq;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.dto.response.UserRes;
import com.ticketbox.auth_service.entity.Role;
import com.ticketbox.auth_service.entity.User;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.RoleMapper;
import com.ticketbox.auth_service.mappers.UserMapper;
import com.ticketbox.auth_service.mapstruct.UserStruct;
import com.ticketbox.auth_service.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    //xml
    UserMapper userMapper;
    RoleMapper roleMapper;

    //struct
    UserStruct userStruct;

    //others
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void createUser(UserCreateReq req) {

        //CHECKING: is user existed?
        if (userMapper.isUserExisted(req.getEmail()) == 1)
            throw new AppException(ErrorEnum.USER_ALREADY_EXISTS);

        //SECURITY: hash password
        User newUser = userStruct.toUser(req);
        log.info("user {}", newUser.toString());
        String hashedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(hashedPassword);

        //Set other attributes
        Role userRole = roleMapper.findRoleByName("USER").orElse(null);
        newUser.setRole(userRole);

        //SECURITY: verify OTP (carry out later)

        //DBMS: save new user
        log.info("user after hashing pass {}", newUser.toString());
        userMapper.createUser(newUser);
    }

    @Override
    @Transactional
    public UserRes updateUser(int userId, UserUpdateReq req) {

        //CHECKING: is user existed
        User existedUser = userMapper.getUserById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        Map<String, Object> filter = new HashMap<>();

        if (Objects.nonNull(req.getEmail())) {
            filter.put("email", req.getEmail());
        }

        if (Objects.nonNull(req.getPhone())) {
            filter.put("phone", req.getEmail());
        }

        if (Objects.nonNull(req.getPassword())) {
            String hashedPassword = passwordEncoder.encode(req.getPassword());
            filter.put("password", hashedPassword);
        }

        if (Objects.nonNull(req.getLastName())) {
            filter.put("lastName", req.getLastName());
        }

        if (Objects.nonNull(req.getFirstName())) {
            filter.put("firstName", req.getFirstName());
        }

        if (Objects.nonNull(req.getGender())) {
            filter.put("gender", req.getGender());
        }

        //UPDATE
        userMapper.updateUser(existedUser.getId(), filter);

        return userStruct.toRes(userMapper.getUserById(existedUser.getId()).orElse(null));
    }

    @Override
    @Transactional
    public void changeUserStatus(int userId, boolean status) {
        //CHECKING: is user existed
        User existedUser = userMapper.getUserById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND));

        Map<String, Object> filter = new HashMap<>();
        filter.put("isActive", status);

        userMapper.updateUser(existedUser.getId(), filter);
    }

    @Override
    public UserRes getUserById(int userId) {
        return userStruct.toRes(userMapper.getUserById(userId)
                .orElseThrow(() -> new AppException(ErrorEnum.USER_NOT_FOUND)));
    }

    @Override
    public PageRes<List<UserRes>> getUsers(int page, int pageSize,
                                           String sort, String direction,
                                           Map<String, Object> filter) {

        List<UserRes> users = userMapper.getUsers(page, (page - 1) * pageSize,
                direction, sort, filter).stream().map(userStruct::toRes).toList();

        int totalCount = userMapper.countTotal(page, (page - 1) * pageSize,
                direction, sort, filter);

        return PageRes.<List<UserRes>>builder()
                .pageSize(pageSize)
                .items(users)
                .totalItem(totalCount)
                .page(page)
                .build();
    }
}
