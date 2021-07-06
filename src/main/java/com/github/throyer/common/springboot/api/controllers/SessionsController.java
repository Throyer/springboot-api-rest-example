package com.github.throyer.common.springboot.api.controllers;

import javax.validation.Valid;

import com.github.throyer.common.springboot.api.domain.services.security.SessionService;
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshTokenRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshTokenResponse;
import com.github.throyer.common.springboot.api.domain.services.security.dto.TokenRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.TokenResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "Session")
@RestController
@RequestMapping("/sessions")
public class SessionsController {

    @Autowired
    private SessionService service;

    @PostMapping
    public ResponseEntity<TokenResponse> create(@RequestBody @Valid TokenRequest request) {
        return service.create(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refresh(@RequestBody @Valid RefreshTokenRequest request) {
        return service.refresh(request);
    }
}
