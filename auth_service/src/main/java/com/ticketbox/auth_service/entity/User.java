package com.ticketbox.auth_service.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    Integer id;
    String email;
    String phone;
    String password;
    String firstName;
    String lastName;
    Boolean isActive;
    Integer gender;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Role role;
}
