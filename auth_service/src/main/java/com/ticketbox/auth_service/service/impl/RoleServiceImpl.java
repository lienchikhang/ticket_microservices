package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Role;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.RoleMapper;
import com.ticketbox.auth_service.mapstruct.RoleStruct;
import com.ticketbox.auth_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    RoleMapper mapper;
    RoleStruct roleStruct;

    @Override
    public RoleRes add(RoleCreateReq role) {

        //checking exist
        if (Objects.nonNull(mapper.findRoleByName(role.getRoleName()).orElse(null))) {
            throw new AppException(ErrorEnum.ROLE_ALREADY_EXISTS);
        }

        //add new role
        mapper.addRole(roleStruct.toRole(role));

        //return
        return roleStruct.toRes(roleStruct.toRole(role));
    }

    @Override
    public RoleRes delete(int id) {
        return null;
    }

    @Override
    public RoleRes update(int id, Role newRole) {
        return null;
    }

    @Override
    public RoleRes get(int id) {
        return null;
    }

    @Override
    public List<RoleRes> getAll() {
        return List.of();
    }


}
