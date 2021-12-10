package com.github.throyer.common.springboot.controllers.app;

import javax.validation.Valid;

import com.github.throyer.common.springboot.domain.services.user.CreateUserService;
import com.github.throyer.common.springboot.domain.services.user.dto.CreateUserApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/register")
public class RegisterController {
    
    @Autowired
    private CreateUserService service;

    @GetMapping(produces = "text/html")
    public String index(Model model) {
        model.addAttribute("user", new CreateUserApp());
        return "app/register/index";
    }

    @PostMapping(produces = "text/html")
    public String create(
        @Valid CreateUserApp user,
        BindingResult result,
        RedirectAttributes redirect,
        Model model
    ) {
        return service.create(user, result, redirect, model);
    }
}
