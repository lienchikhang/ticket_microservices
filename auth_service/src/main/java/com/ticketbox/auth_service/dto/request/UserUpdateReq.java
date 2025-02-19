package com.ticketbox.auth_service.dto.request;

import com.ticketbox.auth_service.validations.NotAlphaNumeric;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateReq {
    @Nullable
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "INVALID_USER_EMAIL")
    String email;

    @Nullable
    @Length(max = 11, min = 10, message = "INVALID_USER_PHONE")
    String phone;

    @Nullable
    @Length(min = 6, message = "INVALID_USER_PASSWORD")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).+$", message = "INVALID_USER_PASSWORD")
    String password;

    @Nullable
    @Length(min = 1, message = "INVALID_USER_NAME")
    @NotAlphaNumeric(message = "INVALID_USER_NAME")
    String firstName;

    @Nullable
    @Length(min = 1, message = "INVALID_USER_NAME")
    @NotAlphaNumeric(message = "INVALID_USER_NAME")
    String lastName;

    @Builder.Default
    Integer gender = 1;
}
