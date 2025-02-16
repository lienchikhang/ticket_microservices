package com.ticketbox.auth_service.service;

import com.ticketbox.auth_service.dto.request.AuthorityCreateReq;
import com.ticketbox.auth_service.dto.request.AuthorityUpdateReq;
import com.ticketbox.auth_service.dto.response.AuthorityRes;
import com.ticketbox.auth_service.dto.response.PageRes;

import java.util.List;

public interface AuthorityService {

    AuthorityRes add(AuthorityCreateReq authorityCreateReq);

    AuthorityRes update(int id, AuthorityUpdateReq authorityUpdateReq);

    PageRes<List<AuthorityRes>> findAll(int page, int pageSize, String sort, String direction);

    AuthorityRes findById(int id);

    AuthorityRes delete(int id);

}
