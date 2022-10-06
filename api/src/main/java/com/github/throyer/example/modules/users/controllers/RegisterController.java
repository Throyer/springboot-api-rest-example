package com.github.throyer.example.modules.users.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.throyer.example.modules.users.dtos.CreateUserProps;
import com.github.throyer.example.modules.users.service.CreateUserService;

@Controller
@RequestMapping("/app/register")
public class RegisterController {

  private final CreateUserService service;

  @Autowired
  public RegisterController(CreateUserService service) {
    this.service = service;
  }

  @GetMapping(produces = "text/html")
  public String index(Model model) {
    model.addAttribute("user", new CreateUserProps());
    return "app/register/index";
  }

  @PostMapping(produces = "text/html")
  public String create(
      @Valid CreateUserProps props,
      BindingResult validations,
      RedirectAttributes redirect,
      Model model) {
    service.create(props, validations, redirect, model);

    if (validations.hasErrors()) {
      return "app/register/index";
    }

    return "redirect:/app/login";
  }
}
