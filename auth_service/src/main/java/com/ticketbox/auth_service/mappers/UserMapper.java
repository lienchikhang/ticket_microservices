package com.ticketbox.auth_service.mappers;

import com.ticketbox.auth_service.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int createUser();

    User getUserById();

    List<User> getUsers();

    int updateUser(int userId);

//    int unActiveUser(int userId);
}
