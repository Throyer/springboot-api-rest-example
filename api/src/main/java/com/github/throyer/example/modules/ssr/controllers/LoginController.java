package com.github.throyer.example.modules.ssr.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/login")
public class LoginController {
  @GetMapping
  public String login() {
    return "app/login/index";
  }
}
