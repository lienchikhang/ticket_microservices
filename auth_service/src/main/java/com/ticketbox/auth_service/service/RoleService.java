package com.ticketbox.auth_service.service;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.request.RoleUpdateReq;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Role;

import java.util.List;

public interface RoleService {

    RoleRes add(RoleCreateReq role);

    RoleRes delete(int id);

    RoleRes update(int id, RoleUpdateReq newRole);

    RoleRes get(int id);

    List<RoleRes> getAll(String direction, int page, int pageSize);
}
