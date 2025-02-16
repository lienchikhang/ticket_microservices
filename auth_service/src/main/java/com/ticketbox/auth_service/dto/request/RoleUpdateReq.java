package com.ticketbox.auth_service.dto.request;

import com.ticketbox.auth_service.validations.NotAlphaNumeric;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateReq {

    @NotAlphaNumeric(message = "INVALID_ROLE_NAME")
    String roleName;

    @Nullable
    Set<String> authorities;

}
