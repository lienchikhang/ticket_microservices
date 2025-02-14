package com.ticketbox.auth_service.exceptionHandler;

import com.ticketbox.auth_service.dto.response.AppResponse;
import com.ticketbox.auth_service.enums.ErrorEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandling {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        log.info("has errror");

        ErrorEnum error;

         try {
             error = ErrorEnum.valueOf(e.getBindingResult().getFieldError().getDefaultMessage());
             log.info("error msg {}", error.getMessage());
             log.info("error code{}", error.getCode());

             return new ResponseEntity<>(AppResponse.builder()
                     .code(error.getCode())
                     .message(error.getMessage())
                     .build(), HttpStatus.BAD_REQUEST);

         } catch (IllegalArgumentException iae) {
             return new ResponseEntity<>(AppResponse.builder()
                     .code(9999)
                     .message("No such error code")
                     .build(), HttpStatus.INTERNAL_SERVER_ERROR);
         }

    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<AppResponse> handleRuntimeException(AppException e) {

        ErrorEnum error = e.getErrorEnum();

        return new ResponseEntity<>(AppResponse.builder()
                .code(error.getCode())
                .statusCode(error.getStatusCode())
                .message(error.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }

}
