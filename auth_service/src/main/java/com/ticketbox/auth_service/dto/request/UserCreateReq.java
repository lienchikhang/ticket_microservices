package com.ticketbox.auth_service.dto.request;

import com.ticketbox.auth_service.entity.Role;
import com.ticketbox.auth_service.validations.NotAlphaNumeric;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateReq {
    @NotEmpty(message = "NOT_EMPTY_EMAIL")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "INVALID_USER_EMAIL")
    String email;

    @NotEmpty(message = "NOT_EMPTY_PHONE")
    @Length(max = 11, min = 10, message = "INVALID_USER_PHONE")
    String phone;

    @NotEmpty(message = "NOT_EMPTY_PASSWORD")
    @Length(min = 6, message = "INVALID_USER_PASSWORD")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).+$", message = "INVALID_USER_PASSWORD")
    String password;

    @NotEmpty(message = "NOT_EMPTY_USERNAME")
    @Length(min = 1, message = "INVALID_USER_NAME")
    @NotAlphaNumeric(message = "INVALID_USER_NAME")
    String firstName;

    @NotEmpty(message = "NOT_EMPTY_USERNAME")
    @Length(min = 1, message = "INVALID_USER_NAME")
    @NotAlphaNumeric(message = "INVALID_USER_NAME")
    String lastName;

    @Builder.Default
    int gender = 1;
}
