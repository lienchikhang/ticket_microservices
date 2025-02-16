package com.ticketbox.auth_service.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public enum ErrorEnum {

    //ROLE
    ROLE_NOT_FOUND(1000, 404, "Role not found!"),
    ROLE_ALREADY_EXISTS(1001, 400, "Role has already exists!"),
    INVALID_ROLE_NAME(1002, 400, "Invalid role name!"),
    NOT_EMPTY_ROLE(1003, 400, "Role must not be empty!"),

    AUTHORITY_NOT_FOUND(1004, 404, "Authority not found!"),
    AUTHORITY_ALREADY_EXISTS(1005, 400, "Authority has already exists!"),
    INVALID_AUTHORITY_NAME(1006, 400, "Invalid Authority name!"),
    NOT_EMPTY_AUTHORITY(1007, 400, "Authority must not be empty!"),
    ONE_OF_AUTHORITY_NOT_FOUND(1008, 404, "One of authorities not found")
    ;

    ErrorEnum(int code, int statusCode, String message) {
        this.code = code;
        this.statusCode = statusCode;
        this.message = message;
    }

    int code;
    int statusCode;
    String message;

}
