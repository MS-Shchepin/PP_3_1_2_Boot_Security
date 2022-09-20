package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.roles.Roles;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;

@Controller
public class MainController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAdminPage(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/admin/")
    public String showEditor(@RequestParam Long id, @RequestParam(defaultValue = "0") Integer errorMessage, Model model) {
        if (id == 0) {
            model.addAttribute("modelUser", new User());
        } else {
            model.addAttribute("modelUser", userService.getById(id));
        }
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("roleList", Roles.getAllRoles());
        return "editor";
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
    public String saveUser(@ModelAttribute User user) {
        String result = "redirect:http://localhost:8080/admin";
        int errorCode = userService.save(user);
        if (errorCode != 0) {
            result = result + "/?id=%d&errorMessage=%d".formatted(user.getId(),errorCode);
        }
        System.out.printf("saveUser -> %s%n", result);
        return result;
    }

    @GetMapping("/user")
    public String showUserPage(Principal principal, Model model) {
        model.addAttribute("modelUser", userService.getUserByUsername(principal.getName()));
        return "user";
    }

    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }

}
