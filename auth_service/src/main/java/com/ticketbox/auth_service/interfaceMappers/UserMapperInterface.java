package com.ticketbox.auth_service.interfaceMappers;

import com.ticketbox.auth_service.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapperInterface {
    Role findRoleById(int roleId);
}
