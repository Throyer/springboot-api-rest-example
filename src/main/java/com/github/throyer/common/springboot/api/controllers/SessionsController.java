package com.github.throyer.common.springboot.api.controllers;

import javax.validation.Valid;

import com.github.throyer.common.springboot.api.domain.services.security.SessionService;
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshSessionRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.RefreshSessionResponse;
import com.github.throyer.common.springboot.api.domain.services.security.dto.SessionRequest;
import com.github.throyer.common.springboot.api.domain.services.security.dto.SessionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "/sessions")
@RestController
@RequestMapping("/sessions")
public class SessionsController {

    @Autowired
    private SessionService service;

    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok", response = SessionResponse.class),
    })
    @PostMapping
    public ResponseEntity<SessionResponse> create(@RequestBody @Valid SessionRequest request) {
        return service.create(request);
    }

    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Ok", response = SessionResponse.class),
    })
    @PostMapping("/refresh")
    public ResponseEntity<RefreshSessionResponse> refresh(@RequestBody @Valid RefreshSessionRequest request) {
        return service.refresh(request);
    }
}
