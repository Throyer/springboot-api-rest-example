package com.github.throyer.common.springboot.controllers.app;

import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.domain.services.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class AppController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public String index(Model model) {
        SecurityService.authorized()
            .ifPresent(session -> repository.findById(session.getId())
                .ifPresent(user -> model.addAttribute("name", user.getName()))
        );
        return "app/index";
    }
}
