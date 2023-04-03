package com.clicks.fulafiaresultcheckingverificationsystem.exceptions;

import com.clicks.fulafiaresultcheckingverificationsystem.utils.ResponseMessage;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@ResponseStatus
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({
            InvalidRequestParamException.class,
            IllegalArgumentException.class,
            java.sql.SQLException.class})
    public ResponseEntity<ResponseMessage> invalidRequestParamExceptionHandler(RuntimeException exception) {

        return ResponseEntity.status(BAD_REQUEST)
                .body(new ResponseMessage("FAILURE", 1, Map.of("message", exception.getMessage())));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            ResourceNotFoundException.class,
            UsernameNotFoundException.class})
    public ResponseEntity<ResponseMessage> notFoundExceptionHandler(RuntimeException exception) {

        return ResponseEntity.status(NOT_FOUND)
                .body(new ResponseMessage("NOT FOUND", 1, Map.of("message", exception.getMessage())));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({
            EmailNotSentException.class,
            ConstraintViolationException.class})
    public ResponseEntity<ResponseMessage> serverErrorExceptionHandler(RuntimeException exception) {

        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(new ResponseMessage("INTERNAL SERVER ERROR", 1, Map.of("message", exception.getMessage())));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ResponseMessage> unauthorizedExceptionHandler(RuntimeException exception) {

        return ResponseEntity.status(UNAUTHORIZED)
                .body(new ResponseMessage("UNAUTHORIZED", 1, Map.of("message", exception.getMessage())));
    }
}
