package com.github.throyer.example.api.domain.authentication.controllers;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithEmailAndPassword;
import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithRefreshToken;
import com.github.throyer.example.api.domain.authentication.models.Authentication;
import com.github.throyer.example.api.domain.authentication.services.CreateAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.throyer.example.api.shared.rest.Responses.ok;

@RestController
@AllArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("/authentication")
public class AuthenticationController {
  private final CreateAuthenticationService service;
  
  @PostMapping
  @Operation(summary = "Create a jwt token")
  public ResponseEntity<Authentication> create(@RequestBody @Valid CreateAuthenticationWithEmailAndPassword body) {
    return ok(service.create(body));
  }

  @PostMapping("/refresh")
  @Operation(summary = "Create a new jwt token from refresh code")
  public ResponseEntity<Authentication> refresh(@RequestBody @Valid CreateAuthenticationWithRefreshToken body) {
    return ok(service.create(body));
  }
}
