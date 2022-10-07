package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.exception_handling.AddUserException;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MainRestController {
    private final UserService userService;

    public MainRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        int errorCode = userService.save(user);
        if (errorCode == 1) {
            throw new AddUserException("Error: Username is already taken");
        }
        return user;
    }

    @PutMapping("/users")
    public User editUser(@RequestBody User user) {
        userService.save(user);
        return user;
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteById(id)
                ? "User %d deleted successfully".formatted(id)
                : "User %d delete failed".formatted(id);
    }
}
