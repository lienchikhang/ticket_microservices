package com.ticketbox.auth_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Authority {
    int authorityId;
    String authorityName;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
