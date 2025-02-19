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
    ONE_OF_AUTHORITY_NOT_FOUND(1008, 404, "One of authorities not found"),

    USER_NOT_FOUND(1009, 404, "User not found!"),
    USER_ALREADY_EXISTS(1010, 400, "User has already exists!"),
    INVALID_USER_NAME(1011, 400, "Invalid username!"),
    NOT_EMPTY_USERNAME(1012, 400, "User must not be empty!"),
    INVALID_USER_PHONE(1013, 400, "Invalid phone number"),
    INVALID_USER_EMAIL(1014, 400, "Invalid email"),
    INVALID_USER_PASSWORD(1015, 400, "Invalid password"),
    NOT_EMPTY_PASSWORD(1016, 400, "Password must not be empty!"),
    NOT_EMPTY_EMAIL(1017, 400, "Password must not be empty!"),
    NOT_EMPTY_PHONE(1018, 400, "Password must not be empty!"),
    NOT_EMPTY_STATUS(1019, 400, "Status must not be empty!"),
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
