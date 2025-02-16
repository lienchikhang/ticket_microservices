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
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AuthorityStruct {

     @Mappings({
             @Mapping(target = "authorityId", ignore = true),
             @Mapping(target = "isActive", ignore = true),
             @Mapping(target = "createdAt", ignore = true),
             @Mapping(target = "updatedAt", ignore = true)
     })
     Authority toAuthority(AuthorityCreateReq authorityCreateReq);

     @Mappings({
             @Mapping(target = "authorityId", ignore = true),
             @Mapping(target = "isActive", ignore = true),
             @Mapping(target = "createdAt", ignore = true),
             @Mapping(target = "updatedAt", ignore = true)
     })
     Authority toAuthority(AuthorityUpdateReq authorityUpdateReq);

     @Mapping(target = "isActive", source = "isActive")
     AuthorityRes toRes(Authority authority);
}
