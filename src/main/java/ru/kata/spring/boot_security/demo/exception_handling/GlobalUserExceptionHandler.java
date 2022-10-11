package ru.kata.spring.boot_security.demo.exception_handling;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalUserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<JsonMessage> handleException(NotFoundUserException exception) {
        return new ResponseEntity<>(new JsonMessage(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
