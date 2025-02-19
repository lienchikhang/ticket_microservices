package com.ticketbox.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ticketbox.auth_service.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRes {
    int id;
    String email;
    String phone;
    String password;
    String firstName;
    String lastName;
    Boolean isActive;
    int gender;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Role role;
}
