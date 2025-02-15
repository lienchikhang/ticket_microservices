package com.ticketbox.auth_service.mappers;

import com.ticketbox.auth_service.entity.Authority;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AuthorityMapper {

    int addAuthority(Authority authority);

    @Select("select * from Authorties where authority_id = #{id}")
    Optional<Authority> findById(int id);

    @Select("select distinct count(authority_name) from Authorties where authority_name = #{name}")
    int isNameExist(String name);

    @Select("select count(authority_id) from Authorities where is_active = 1")
    int count();

    List<Authority> findAll(@Param("pageSize") int pageSize,
                            @Param("offSet") int offSet,
                            @Param("sort") String sort,
                            @Param("direction") String direction);

    int updateAuthority(Authority authority);

}
