package com.ticketbox.auth_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginReq {
    @NotEmpty(message = "NOT_EMPTY_EMAIL")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "INVALID_USER_EMAIL")
    String email;

    @NotEmpty(message = "NOT_EMPTY_PASSWORD")
    String password;
}
