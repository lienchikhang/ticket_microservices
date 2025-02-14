package com.ticketbox.auth_service.mappers;

import com.ticketbox.auth_service.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface RoleMapper {

    int addRole(Role role);

    Optional<Role> findRoleById(int roleId);

    Optional<Role> findRoleByName(String name);

    List<Role> findAll(@Param("direction") String direction,
                       @Param("offSet") int offSet,
                       @Param("pageSize") int pageSize);

    int updateRole(Role role);
}
