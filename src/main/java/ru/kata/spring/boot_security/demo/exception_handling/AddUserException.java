package ru.kata.spring.boot_security.demo.exception_handling;

public class AddUserException extends RuntimeException {
    public AddUserException(String message) {
        super(message);
    }
}
