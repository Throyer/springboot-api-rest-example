package com.github.throyer.example.api.domain.authentication.controllers;

import static com.github.throyer.example.api.shared.rest.Responses.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithEmailAndPassword;
import com.github.throyer.example.api.domain.authentication.models.Authentication;
import com.github.throyer.example.api.domain.authentication.services.CreateAuthenticationWithEmailAndPasswordService;
import com.github.throyer.example.api.domain.authentication.swagger.EmailNotConfirmedResponse;
import com.github.throyer.example.api.domain.authentication.swagger.UsernameOrPasswordInvalidResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("/authentication")
public class CreateAuthenticationWithEmailAndPasswordController {
  private final CreateAuthenticationWithEmailAndPasswordService service;

  @PostMapping
  @ApiResponse(
    responseCode = "403",
    description = """
      Incorrect username or password,
      also happens when the `email` was not confirmed by the user.
    """,
    content = {
    @Content(schema = @Schema(oneOf = {
      UsernameOrPasswordInvalidResponse.class,
      EmailNotConfirmedResponse.class
    }))
  })
  @Operation(summary = "Create a jwt token")
  public ResponseEntity<Authentication> create(@RequestBody @Valid CreateAuthenticationWithEmailAndPassword body) {
    log.info("creating new session");
    var session = service.create(body);
    log.info("session successfully created");
    return ok(session);
  }
}
