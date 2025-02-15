package com.ticketbox.auth_service.mapstruct;

import com.ticketbox.auth_service.dto.request.AuthorityCreateReq;
import com.ticketbox.auth_service.dto.request.AuthorityUpdateReq;
import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.response.AuthorityRes;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Authority;
import com.ticketbox.auth_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthorityStruct {


     Authority toAuthority(AuthorityCreateReq authorityCreateReq);

     Authority toAuthority(AuthorityUpdateReq authorityUpdateReq);

     @Mapping(target = "isActive", source = "isActive")
     AuthorityRes toRes(Authority authority);
}
