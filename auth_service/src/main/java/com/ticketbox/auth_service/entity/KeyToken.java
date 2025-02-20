package com.ticketbox.auth_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyToken {
    Integer userId;
    String publicKey;
    String refreshToken;
}
