package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String showJsAdminPage() {
        return "admin";
    }

    @GetMapping("/oldadmin")
    public String showTlAdminPage(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("roleList", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "oldadmin";
    }

    @GetMapping("oldadmin/deleteUser")
    public String performDelete(@RequestParam Long id) {
        if (userService.deleteById(id)) {
            System.out.printf("user id = %d removed%n",id);
        } else {
            System.out.printf("user id = %d does not exist%n",id);
        }
        return "redirect:http://localhost:8080/oldadmin";
    }

    @PostMapping("oldadmin/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user) {
        String result = "redirect:http://localhost:8080/oldadmin";
        System.out.println(user);
        int errorCode = userService.save(user);
        if (errorCode != 0) {
            result = result + "/?id=%d&errorMessage=%d".formatted(user.getId(),errorCode);
        }
        System.out.printf("saveUser -> %s%n", result);
        return result;
    }
}
