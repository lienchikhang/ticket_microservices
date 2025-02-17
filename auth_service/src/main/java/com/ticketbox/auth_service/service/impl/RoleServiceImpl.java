package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.request.RoleUpdateReq;
import com.ticketbox.auth_service.dto.response.AuthorityRes;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Authority;
import com.ticketbox.auth_service.entity.Role;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.AuthorityMapper;
import com.ticketbox.auth_service.mappers.RoleMapper;
import com.ticketbox.auth_service.mapstruct.AuthorityStruct;
import com.ticketbox.auth_service.mapstruct.RoleStruct;
import com.ticketbox.auth_service.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    RoleMapper mapper;
    AuthorityMapper authorityMapper;
    RoleStruct roleStruct;
    AuthorityStruct authorityStruct;

    @Override
    @Transactional
    public RoleRes add(RoleCreateReq role) {

        //checking exist
        if (Objects.nonNull(mapper.findRoleByName(role.getRoleName()).orElse(null))) {
            throw new AppException(ErrorEnum.ROLE_ALREADY_EXISTS);
        }

        Set<Authority> newAuthorities = new HashSet<>();
        role.getAuthorities().forEach((au) -> {
            Authority authority = authorityMapper.findByName(au)
                    .orElseThrow(() -> new AppException(ErrorEnum.AUTHORITY_NOT_FOUND));

            newAuthorities.add(authority);
        });

        //add new role
        mapper.addRole(roleStruct.toRole(role));
        Role r = mapper.findRoleByName(role.getRoleName())
                .orElseThrow(() -> new AppException(ErrorEnum.AUTHORITY_NOT_FOUND));
        mapper.addAuthorities(r.getRoleId(),newAuthorities);

        //return
        return roleStruct.toRes(roleStruct.toRole(role));
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
    @Transactional
    public RoleRes update(int id, RoleUpdateReq roleUpdateReq) {

        //checking exist
        Role role = mapper.findRoleById(id).orElseThrow(() -> new AppException(ErrorEnum.ROLE_NOT_FOUND));

        //checking props
        if (Objects.nonNull(roleUpdateReq.getRoleName())) {
            role.setRoleName(roleUpdateReq.getRoleName());
        }

        Set<Authority> newAuthorities = new HashSet<>();
        if (Objects.nonNull(roleUpdateReq.getAuthorities())) {
            roleUpdateReq.getAuthorities()
                    .forEach((au) -> {
                        newAuthorities.add(authorityMapper.findByName(au)
                                .orElseThrow(() -> new AppException(ErrorEnum.AUTHORITY_NOT_FOUND)));
                    });

            mapper.addAuthorities(role.getRoleId(), newAuthorities);
        }

        //update
        mapper.updateRole(role);

        //result
        RoleRes res = roleStruct.toRes(mapper.findRoleWithAuthoritiesById(role.getRoleId())
                .orElseThrow(() -> new AppException(ErrorEnum.ROLE_NOT_FOUND)));
        res.setAuthorities(newAuthorities);

        return res;
    }

    @Override
    public RoleRes get(int id) {
        return roleStruct.toRes(mapper.findRoleById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.ROLE_NOT_FOUND)));
    }

    @Override
    public PageRes<List<RoleRes>> getAll(String direction, int page, int pageSize) {

        List<RoleRes> roles = mapper.findAll(direction, (page - 1) * pageSize, pageSize)
                .stream().map((role) -> {
            RoleRes ri = roleStruct.toRes(role);
            if (Objects.nonNull(role.getAuthorities())) {
                Set<Authority> authorities = new HashSet<>();
                authorities.addAll(role.getAuthorities());
                ri.setAuthorities(authorities);
            }
            return ri;
        }).toList();

        int totalItem = mapper.count();

        return PageRes.<List<RoleRes>>builder()
                .page(page)
                .pageSize(pageSize)
                .items(roles)
                .totalItem(totalItem)
                .build();
    }


}
