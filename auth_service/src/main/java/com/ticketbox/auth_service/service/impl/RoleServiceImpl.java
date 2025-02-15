package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.request.RoleUpdateReq;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Authority;
import com.ticketbox.auth_service.entity.Role;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.AuthorityMapper;
import com.ticketbox.auth_service.mappers.RoleMapper;
import com.ticketbox.auth_service.mapstruct.RoleStruct;
import com.ticketbox.auth_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    RoleMapper mapper;
    AuthorityMapper authorityMapper;
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
    public RoleRes addAuthorities(List<Authority> authors) {
        return null;
    }

    @Override
    public RoleRes delete(int id) {
        //checking exist
        Role role = mapper.findRoleById(id).orElseThrow(() -> new AppException(ErrorEnum.ROLE_NOT_FOUND));

        //update
        role.setIsActive(false);
        mapper.updateRole(role);

        return roleStruct.toRes(role);
    }

    @Override
    public RoleRes update(int id, RoleUpdateReq roleUpdateReq) {

        //checking exist
        Role role = mapper.findRoleById(id).orElseThrow(() -> new AppException(ErrorEnum.ROLE_NOT_FOUND));

        //update
        role.setRoleName(roleUpdateReq.getRoleName());
        mapper.updateRole(role);

        return roleStruct.toRes(role);
    }

    @Override
    public RoleRes get(int id) {
        return roleStruct.toRes(mapper.findRoleById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.ROLE_NOT_FOUND)));
    }

    @Override
    public PageRes<List<RoleRes>> getAll(String direction, int page, int pageSize) {

        List<RoleRes> roles = mapper.findAll(direction, (page - 1) * pageSize, pageSize).stream()
                .map(roleStruct::toRes).toList();

        int totalItem = mapper.count();

        return PageRes.<List<RoleRes>>builder()
                .page(page)
                .pageSize(pageSize)
                .items(roles)
                .totalItem(totalItem)
                .build();
    }


}
