package com.github.throyer.common.springboot.controllers.api;

import com.github.throyer.common.springboot.utils.Hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping
    public Hello index() {
        return () -> "Is a live!";
    }
}
