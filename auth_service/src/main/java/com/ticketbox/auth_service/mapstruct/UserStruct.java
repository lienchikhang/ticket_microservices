package com.ticketbox.auth_service.mapstruct;

import com.ticketbox.auth_service.dto.request.UserCreateReq;
import com.ticketbox.auth_service.dto.request.UserUpdateReq;
import com.ticketbox.auth_service.dto.response.UserRes;
import com.ticketbox.auth_service.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface UserStruct {

    @Mappings({
            @Mapping(target = "role", ignore = true)
    })
    User toUser(UserCreateReq req);

    User toUser(UserUpdateReq req);

    UserRes toRes(User user);
}
