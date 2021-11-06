package com.github.throyer.common.springboot.controllers.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Status check")
@RestController
@RequestMapping("/api")
public class ApiController {

    public class StatusCheck {
        private final String message = "Is a live!"; 
        public String getMessage() {
            return message;
        }
    }

    private StatusCheck status = new StatusCheck();
    
    @GetMapping
    public StatusCheck index() {        
        return status;
    }
}