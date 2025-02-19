package com.ticketbox.auth_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRes {
    String accessToken;
    String freshToken;
    LocalDateTime at;
}
