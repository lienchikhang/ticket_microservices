package com.ticketbox.auth_service.dto.response;

import com.ticketbox.auth_service.entity.Authority;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRes {
    int roleId;
    String roleName;
    Boolean isActive;
    Set<Authority> authorities;
}
