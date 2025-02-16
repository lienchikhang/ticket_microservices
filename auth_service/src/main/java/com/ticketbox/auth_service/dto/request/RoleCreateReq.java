package com.ticketbox.auth_service.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateReq {
    @NotEmpty(message = "NOT_EMPTY_ROLE")
    String roleName;
    Set<String> authorities;
}
