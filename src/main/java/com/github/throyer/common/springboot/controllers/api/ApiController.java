package com.github.throyer.common.springboot.controllers.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import com.github.throyer.common.springboot.utils.Hello;

@Api(tags = "Status check", produces = "application/json")
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping
    public Hello index() {
        return () -> "Is a live!";
    }
}
