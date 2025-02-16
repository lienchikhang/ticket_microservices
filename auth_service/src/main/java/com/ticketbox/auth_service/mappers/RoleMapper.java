package com.ticketbox.auth_service.mappers;

import com.ticketbox.auth_service.entity.Authority;
import com.ticketbox.auth_service.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Mapper
public interface RoleMapper {

    int addRole(Role role);

    void addAuthorities(@Param("roleId") int roleId,
                        @Param("authorities") Set<Authority> authorities);

    Optional<Role> findRoleById(int roleId);

    Optional<Role> findRoleWithAuthoritiesById(int roleId);

    Optional<Role> findRoleByName(String name);

    List<Role> findUnsignedAuthoritiesByRoleId(int roleId);

    List<Role> findAll(@Param("direction") String direction,
                       @Param("offSet") int offSet,
                       @Param("pageSize") int pageSize);

    @Select("select count(role_id) from Roles")
    int count();

    int updateRole(Role role);
}
