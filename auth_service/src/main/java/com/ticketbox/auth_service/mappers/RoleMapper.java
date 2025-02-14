package com.ticketbox.auth_service.mappers;

import com.ticketbox.auth_service.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface RoleMapper {

    int addRole(Role role);

    Optional<Role> findRoleById(int roleId);

    Optional<Role> findRoleByName(String name);
}
