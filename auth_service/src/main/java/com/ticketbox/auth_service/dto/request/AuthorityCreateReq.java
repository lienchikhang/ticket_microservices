package com.ticketbox.auth_service.dto.request;

import com.ticketbox.auth_service.validations.NotAlphaNumeric;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthorityCreateReq {
    @NotEmpty(message = "NOT_EMPTY_AUTHORITY")
    @NotAlphaNumeric(message = "INVALID_AUTHORITY_NAME")
    String authorityName;
}
