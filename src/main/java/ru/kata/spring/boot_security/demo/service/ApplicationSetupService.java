package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

@Service
public class ApplicationSetupService {
    private UserService userService;
    List<Role> allRoles;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void setupApplication() {
        setApplicationRoles();
        createSuperAdmin();
    }

    public void createSuperAdmin() {
        User superAdmin = new User("Admin", "Super", 0, "admin@admin", "admin");
        superAdmin.setRoles(allRoles);
        int errorCode = userService.save(superAdmin);
        if (errorCode == 0) {
            System.out.println("SuperAdmin created successfully.");
        }
        if (errorCode == 1) {
            System.out.println("SuperAdmin exists.");
        }
    }

    private void setApplicationRoles() {
        allRoles = List.of(
                new Role("ROLE_SUPER_ADMIN"),
                new Role("ROLE_ADMIN"),
                new Role("ROLE_USER"),
                new Role("READ_NEWS"));
    }

}
