package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.exception_handling.JsonMessage;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainRestController {
    private final UserService userService;
    private final RoleService roleService;

    public MainRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/users")
    public ResponseEntity<JsonMessage> addUser(@RequestBody User user) {
        user.setRoles(user.getRoles().stream().map(role -> roleService.findById(role.getId())).toList());
        return userService.save(user) == 0
                ? new ResponseEntity<>(new JsonMessage(String.valueOf(user.getId())), HttpStatus.OK)
                : new ResponseEntity<>(new JsonMessage("Error: username %s already taken".formatted(user.getUsername())),
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/users")
    public ResponseEntity<JsonMessage> editUser(@RequestBody User user) {
        return userService.save(user) == 0
                ? new ResponseEntity<>(new JsonMessage(String.valueOf(user.getId())), HttpStatus.OK)
                : new ResponseEntity<>(new JsonMessage("Error: username %s already taken".formatted(user.getUsername())),
                HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<JsonMessage> deleteUser(@PathVariable Long id) {
        return userService.deleteById(id)
                ? new ResponseEntity<>(new JsonMessage("User %d deleted successfully".formatted(id)), HttpStatus.OK)
                : new ResponseEntity<>(new JsonMessage("User %d delete failed".formatted(id)), HttpStatus.BAD_REQUEST);
    }
}
