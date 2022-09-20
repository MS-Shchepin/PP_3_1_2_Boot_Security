package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.kata.spring.boot_security.demo.roles.Roles;

import java.util.List;

@Controller
public class TestController {

    @GetMapping("/test")
    public String showNews(Model model) {
        model.addAttribute("testList", Roles.getAllNames());
        model.addAttribute("source", new Source());
        return "test";
    }

    @RequestMapping(value="/manualTest", method= RequestMethod.POST)
    public String greetingSubmit(@ModelAttribute Source source, Model model) {
        System.out.println(source.getTestList());
        model.addAttribute("source", source);
        return "redirect:test";
    }
   private static class Source {
        private List<String> testList;
        public List<String> getTestList()
        {
            return testList;
        }

    }
}

