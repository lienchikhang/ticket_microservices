package com.ticketbox.auth_service.entity;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    int roleId;
    String roleName;
    Boolean isActive;
    Set<Authority> authorities;
}


