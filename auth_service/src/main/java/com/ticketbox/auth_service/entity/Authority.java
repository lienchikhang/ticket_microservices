package com.ticketbox.auth_service.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Authority {
    int authorityId;
    String authorityName;
    Boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
