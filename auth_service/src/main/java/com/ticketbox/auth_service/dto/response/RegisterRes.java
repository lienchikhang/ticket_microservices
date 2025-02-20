package com.ticketbox.auth_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ticketbox.auth_service.entity.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisterRes {
    User user;
    Map<String, Object> tokens;
}
