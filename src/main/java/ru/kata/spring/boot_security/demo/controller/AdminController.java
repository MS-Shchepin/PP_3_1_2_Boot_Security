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
    private UserService userService;

    private RoleService roleService;

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("roleList", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @GetMapping("admin/deleteUser")
    public String performDelete(@RequestParam Long id) {
        if (userService.deleteById(id)) {
            System.out.printf("user id = %d removed%n",id);
        } else {
            System.out.printf("user id = %d does not exist%n",id);
        }
        return "redirect:http://localhost:8080/admin";
    }

    @PostMapping("admin/saveUser")
    public String saveUser(@ModelAttribute("newUser") User user) {
        String result = "redirect:http://localhost:8080/admin";
        System.out.println(user);
        int errorCode = userService.save(user);
        if (errorCode != 0) {
            result = result + "/?id=%d&errorMessage=%d".formatted(user.getId(),errorCode);
        }
        System.out.printf("saveUser -> %s%n", result);
        return result;
    }
}
