package com.ticketbox.auth_service.mappers;

import com.ticketbox.auth_service.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface UserMapper {

    Integer createUser(User user);

    Optional<User> getUserById(Integer userId);

    Integer countTotal(@Param("limit") int limit, @Param("offSet") int offSet,
                       @Param("direction") String direction, @Param("sort") String sort,
                       @Param("filter") Map<String, Object> filter);

    List<User> getUsers(@Param("limit") int limit, @Param("offSet") int offSet,
                        @Param("direction") String direction, @Param("sort") String sort,
                        @Param("filter") Map<String, Object> filter);

    Integer updateUser(@Param("userId") Integer userId, @Param("filter")Map<String, Object> filter);

    Integer isUserExisted(String email);
}
