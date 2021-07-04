package com.github.throyer.common.springboot.api.controllers;

import com.github.throyer.common.springboot.api.domain.services.user.RecoveryPasswordService;
import com.github.throyer.common.springboot.api.domain.services.user.dto.RecoveryConfirm;
import com.github.throyer.common.springboot.api.domain.services.user.dto.RecoveryRequest;
import com.github.throyer.common.springboot.api.domain.services.user.dto.RecoveryUpdate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Password recovery")
@RestController
@RequestMapping("/recoveries")
public class RecoveriesController {

    @Autowired
    private RecoveryPasswordService service;

    @PostMapping
    public void index(@RequestBody RecoveryRequest request) {        
        service.recovery(request.getEmail());
    }

    @PostMapping("/confirm")
    public void index(@RequestBody RecoveryConfirm confirm) {        
        service.confirm(confirm.getEmail(), confirm.getCode());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/update")
    public void index(@RequestBody RecoveryUpdate update) {        
        service.update(update.getEmail(), update.getCode(), update.getPassword());
    }
}
