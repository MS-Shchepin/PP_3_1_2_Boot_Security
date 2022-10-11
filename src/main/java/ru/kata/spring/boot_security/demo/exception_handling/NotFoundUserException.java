package ru.kata.spring.boot_security.demo.exception_handling;

public class NotFoundUserException extends RuntimeException {
    public NotFoundUserException(String message) {
        super(message);
    }
}
