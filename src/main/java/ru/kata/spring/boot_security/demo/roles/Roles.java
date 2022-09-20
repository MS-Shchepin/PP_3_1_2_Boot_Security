package ru.kata.spring.boot_security.demo.roles;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Roles {
    READ_NEWS("READ_NEWS"),
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    public static List<String> getAllNames() {
        return Arrays.stream(Roles.values()).map(r -> r.name).toList();
    }

    public static List<Role> getAllRoles() {
        return Arrays.stream(Roles.values()).map(r -> new Role(r.name)).toList();
    }

    private final String name;

    Roles(String name) {
        this.name = name;
    }

    public Role role() {
        return new Role(name);
    }
}
