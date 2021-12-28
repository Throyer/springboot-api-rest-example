package com.github.throyer.common.springboot.controllers.app;

import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import static com.github.throyer.common.springboot.domain.services.security.SecurityService.authorized;
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
        authorized()
            .ifPresent(session -> repository.findNameById(session.getId())
                .ifPresent(name -> model.addAttribute("name", name)));
        
        return "app/index";
    }
}
