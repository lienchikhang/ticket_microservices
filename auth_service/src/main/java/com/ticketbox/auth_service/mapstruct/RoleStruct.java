package com.ticketbox.auth_service.mapstruct;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleStruct {
     Role toRole(RoleCreateReq roleCreateReq);

     RoleRes toRes(Role role);
}
