package com.ticketbox.auth_service.service.impl;

import com.ticketbox.auth_service.dto.request.AuthorityCreateReq;
import com.ticketbox.auth_service.dto.request.AuthorityUpdateReq;
import com.ticketbox.auth_service.dto.response.AuthorityRes;
import com.ticketbox.auth_service.dto.response.PageRes;
import com.ticketbox.auth_service.enums.ErrorEnum;
import com.ticketbox.auth_service.exceptionHandler.AppException;
import com.ticketbox.auth_service.mappers.AuthorityMapper;
import com.ticketbox.auth_service.service.AuthorityService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    AuthorityMapper authorityMapper;

    @Override
    public AuthorityRes add(AuthorityCreateReq authorityCreateReq) {

        //check exist
        if (authorityMapper.isNameExist(authorityCreateReq.getAuthorityName()) == 1)
            throw new AppException(ErrorEnum.AUTHORITY_ALREADY_EXISTS);

        //add
//        authorityMapper.addAuthority()

        return null;
    }

    @Override
    public AuthorityRes update(AuthorityUpdateReq authorityUpdateReq) {
        return null;
    }

    @Override
    public PageRes<List<AuthorityRes>> findAll(int page, int pageSize, String sort, String direction) {
        return null;
    }

    @Override
    public AuthorityRes findById(int id) {
        return null;
    }

    @Override
    public AuthorityRes delete(int id) {
        return null;
    }
}
