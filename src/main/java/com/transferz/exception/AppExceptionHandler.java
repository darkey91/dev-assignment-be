package com.transferz.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.Map;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException() {
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransferzException.class)
    public ResponseEntity<Map<String, String>> handleException(final TransferzException ex) {
        var errors = Map.of("Error: ", ex.getMessage()) ;
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
