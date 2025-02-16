package com.ticketbox.auth_service.mapstruct;

import com.ticketbox.auth_service.dto.request.RoleCreateReq;
import com.ticketbox.auth_service.dto.response.RoleRes;
import com.ticketbox.auth_service.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface RoleStruct {
     @Mappings({
             @Mapping(target = "roleId", ignore = true),
             @Mapping(target = "isActive", ignore = true)
     })
     Role toRole(RoleCreateReq roleCreateReq);

     @Mapping(target = "isActive", source = "isActive")
     RoleRes toRes(Role role);
}
