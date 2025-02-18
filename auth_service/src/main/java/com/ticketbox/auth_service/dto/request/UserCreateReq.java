package com.ticketbox.auth_service.dto.request;

import com.ticketbox.auth_service.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateReq {
    @NotEmpty(message = "NOT_EMPTY_EMAIL")
    String email;

    @NotEmpty(message = "NOT_EMPTY_PHONE")
    String phone;

    @NotEmpty(message = "NOT_EMPTY_PASSWORD")
    String password;

    @NotEmpty(message = "NOT_EMPTY_USERNAME")
    String firstName;

    @NotEmpty(message = "NOT_EMPTY_USERNAME")
    String lastName;

    int gender = 1;
}
