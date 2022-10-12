package com.github.throyer.example.modules.authentication.controllers;

import static com.github.throyer.example.modules.infra.http.Responses.ok;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.example.modules.authentication.dtos.CreateAuthenticationWithEmailAndPassword;
import com.github.throyer.example.modules.authentication.dtos.CreateAuthenticationWithRefreshToken;
import com.github.throyer.example.modules.authentication.models.Authentication;
import com.github.throyer.example.modules.authentication.services.CreateAuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Authentication")
@RequestMapping("/api/authentication")
public class AuthenticationController {
  private final CreateAuthenticationService service;

  @Autowired
  public AuthenticationController(CreateAuthenticationService service) {
    this.service = service;
  }

  @PostMapping
  @Operation(summary = "Create a jwt token")
  public ResponseEntity<Authentication> create(
    @RequestBody
    @Valid
    CreateAuthenticationWithEmailAndPassword body
  ) {
    return ok(service.create(body));
  }

  @PostMapping("/refresh")
  @Operation(summary = "Create a new jwt token from refresh code")
  public ResponseEntity<Authentication> refresh(
    @RequestBody
    @Valid
    CreateAuthenticationWithRefreshToken body
  ) {
    return ok(service.create(body));
  }
}