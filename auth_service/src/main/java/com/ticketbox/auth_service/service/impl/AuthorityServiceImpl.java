package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.dto.request.AuthorityCreateReq;
import com.ticketbox.auth_service.dto.request.AuthorityUpdateReq;
import com.ticketbox.auth_service.dto.response.AuthorityRes;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.entity.Authority;
import com.ticketbox.auth_service.entity.Role;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.AuthorityMapper;
import com.ticketbox.auth_service.mappers.RoleMapper;
import com.ticketbox.auth_service.mapstruct.AuthorityStruct;
import com.ticketbox.auth_service.service.AuthorityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    AuthorityMapper authorityMapper;
    RoleMapper roleMapper;
    AuthorityStruct authorityStruct;

    @Override
    @Transactional
    public AuthorityRes add(AuthorityCreateReq authorityCreateReq) {

        //check exist
        if (authorityMapper.isNameExist(authorityCreateReq.getAuthorityName()) == 1)
            throw new AppException(ErrorEnum.AUTHORITY_ALREADY_EXISTS);

        //add
        authorityMapper.addAuthority(authorityStruct.toAuthority(authorityCreateReq));

        return authorityStruct.toRes(authorityMapper.findByName(authorityCreateReq.getAuthorityName()).orElse(null));
    }

    @Override
    @Transactional
    public AuthorityRes update(int id, AuthorityUpdateReq authorityUpdateReq) {

        //check exist
        Authority authority = authorityMapper.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.AUTHORITY_NOT_FOUND));

        if (Objects.nonNull(authorityUpdateReq.getAuthorityName())) {
            authority.setAuthorityName(authorityUpdateReq.getAuthorityName());
        }

        //update
        authorityMapper.updateAuthority(authority);

        return authorityStruct.toRes(authorityMapper.findById(id).orElse(null));
    }

    @Override
    public List<AuthorityRes> getUnsignedAuthoritiesByRoleId(int id) {
        //check exist
        Role existedRole = roleMapper.findRoleById(id).orElseThrow(() -> new AppException(ErrorEnum.ROLE_NOT_FOUND));

        List<AuthorityRes> unSignAuthorities = authorityMapper.findUnsignedAuthoritiesByRoleId(existedRole.getRoleId())
                .stream().map(authorityStruct::toRes).toList();

        return unSignAuthorities;
    }

    @Override
    public PageRes<List<AuthorityRes>> findAll(int page, int pageSize, String sort, String direction) {

        log.info("sort {}", sort);
        log.info("direction {}", direction);

        //get authorities
        List<AuthorityRes> authorityResList = authorityMapper.findAll(pageSize, (page - 1) * pageSize, sort, direction)
                .stream().map(authorityStruct::toRes).toList();

        //get totalItem
        int totalItem = authorityMapper.count();

        return PageRes.<List<AuthorityRes>>builder()
                .totalItem(totalItem)
                .items(authorityResList)
                .pageSize(pageSize)
                .page(page)
                .build();
    }

    @Override
    public AuthorityRes findById(int id) {
        return authorityStruct.toRes(authorityMapper.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.AUTHORITY_NOT_FOUND)));
    }

    @Override
    public AuthorityRes delete(int id) {

        //check exist
        Authority authority = authorityMapper.findById(id)
                .orElseThrow(() -> new AppException(ErrorEnum.AUTHORITY_NOT_FOUND));

        //update
        authority.setIsActive(false);
        authorityMapper.updateAuthority(authority);

        return authorityStruct.toRes(authorityMapper.findById(id).orElse(null));
    }
}
