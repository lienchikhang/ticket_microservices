package com.ticketbox.auth_service.exceptionHandler;

import com.ticketbox.auth_service.enums.ErrorEnum;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.flywaydb.core.api.ErrorCode;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AppException extends RuntimeException {
    ErrorEnum errorEnum;
}
