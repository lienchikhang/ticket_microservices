package com.ticketbox.auth_service.service;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.request.RoleUpdateReq;
import com.ticketbox.auth_service.dto.response.AuthorityRes;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Authority;
import com.ticketbox.auth_service.entity.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleRes add(RoleCreateReq role);

    RoleRes delete(int id);

    RoleRes update(int id, RoleUpdateReq newRole);

    RoleRes get(int id);

    PageRes<List<RoleRes>> getAll(String direction, int page, int pageSize);
}
