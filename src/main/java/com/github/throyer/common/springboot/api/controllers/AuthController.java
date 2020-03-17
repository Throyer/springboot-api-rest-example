package com.github.throyer.common.springboot.api.controllers;

import static com.github.throyer.common.springboot.api.utils.Responses.BadRequest;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;

import javax.validation.Valid;

import com.github.throyer.common.springboot.api.models.security.Login;
import com.github.throyer.common.springboot.api.models.validation.SimpleError;
import com.github.throyer.common.springboot.api.services.TokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@Api(tags = "/auth/token", description = "Token JWT")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private TokenService service;

    @PostMapping("/token")
    public ResponseEntity<?> getToken(@RequestBody @Valid Login login) {

        try {            
            
            return ok(service.buildToken(login));
        } catch (BadCredentialsException exception) {  

            /* usuario invalido ou não autorizado */
            return BadRequest(new SimpleError(null, "Senha ou Usuario invalidos."));
        }
    }
}