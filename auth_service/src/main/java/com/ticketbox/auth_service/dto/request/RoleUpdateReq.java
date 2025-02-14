package com.ticketbox.auth_service.dto.request;

import com.ticketbox.auth_service.validations.NotAlphaNumeric;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleUpdateReq {
    @NotEmpty(message = "NOT_EMPTY_ROLE")
    @NotAlphaNumeric(message = "INVALID_ROLE_NAME")
    String roleName;
}
