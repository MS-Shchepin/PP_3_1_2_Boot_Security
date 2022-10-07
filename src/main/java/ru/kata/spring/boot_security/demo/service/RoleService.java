package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.Role;

import java.util.List;

public interface RoleService {

    boolean save(Role role);
    Role findById(long id);
    List<Role> getAllRoles();
    boolean deleteById(long id);

}
