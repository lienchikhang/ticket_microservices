package com.ticketbox.auth_service.mapstruct;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleStruct {
     Role toRole(RoleCreateReq roleCreateReq);

     @Mapping(target = "isActive", source = "isActive")
     RoleRes toRes(Role role);
}
