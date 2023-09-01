package com.github.throyer.example.api.domain.user.controllers;

import com.github.throyer.example.api.domain.user.dtos.CreateUserData;
import com.github.throyer.example.api.domain.user.dtos.UserInformation;
import com.github.throyer.example.api.domain.user.services.CreateUserService;
import com.github.throyer.example.api.domain.user.swagger.CreateUserConflictResponse;
import com.github.throyer.example.api.infra.handlers.swagger.BadRequestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.github.throyer.example.api.shared.rest.Responses.created;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@Tag(name = "Users")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class CreateUserController {
  private final CreateUserService service;

  @PostMapping
  @ResponseStatus(CREATED)
  @ApiResponse(
    responseCode = "400",
    description = "when user creation occurs with invalid fields.",
    content = {@Content(schema = @Schema(implementation = BadRequestResponse.class))}
  )
  @ApiResponse(
    responseCode = "409",
    description = "e-mail unavailable",
    content = {@Content(schema = @Schema(implementation = CreateUserConflictResponse.class))}
  )
  @Operation(summary = "Register a new user", description = "Creates a new user")
  public ResponseEntity<UserInformation> create(@RequestBody @Valid CreateUserData props) {
    log.info("creating a new user.");
    var user = service.create(props);

    log.info("user successfully created.");
    return created(new UserInformation(user));
  }
}
