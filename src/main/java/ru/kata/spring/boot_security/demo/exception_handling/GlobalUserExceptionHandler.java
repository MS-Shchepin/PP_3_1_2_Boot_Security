package ru.kata.spring.boot_security.demo.exception_handling;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalUserExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<JsonErrorMessage> handleException(AddUserException exception) {
        return new ResponseEntity<>(new JsonErrorMessage(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
