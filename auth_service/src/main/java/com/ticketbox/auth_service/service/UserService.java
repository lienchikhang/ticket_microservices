package com.ticketbox.auth_service.service;

import com.ticketbox.auth_service.dto.request.UserCreateReq;
import com.ticketbox.auth_service.dto.request.UserUpdateReq;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.dto.response.UserRes;

import java.util.List;
import java.util.Map;

public interface UserService {

    void createUser(UserCreateReq req);

    UserRes updateUser(int userId, UserUpdateReq req);

    void changeUserStatus(int userId, boolean status);

    UserRes getUserById(int userId);

    PageRes<List<UserRes>> getUsers(int page, int pageSize, String sort, String direction, Map<String, Object> filter);
}
