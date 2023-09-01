package com.github.throyer.example.api.domain.authentication.controllers;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithRefreshToken;
import com.github.throyer.example.api.domain.authentication.models.Authentication;
import com.github.throyer.example.api.domain.authentication.services.CreateAuthenticationWithRefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.throyer.example.api.shared.rest.Responses.ok;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("/authentication")
public class CreateAuthenticationWithRefreshTokenController {
  private final CreateAuthenticationWithRefreshTokenService service;
  
  @PostMapping("/refresh")
  @Operation(summary = "Create a new jwt token from refresh code")
  public ResponseEntity<Authentication> refresh(@RequestBody @Valid CreateAuthenticationWithRefreshToken body) {
    log.info("refreshing a session");
    var session = service.create(body);
    log.info("session successfully refresh");
    return ok(session);
  }
}
