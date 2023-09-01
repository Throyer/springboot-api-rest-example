package com.github.throyer.example.api.domain.user.controllers;

import com.github.throyer.example.api.domain.user.dtos.UpdateUserData;
import com.github.throyer.example.api.domain.user.dtos.UserInformation;
import com.github.throyer.example.api.domain.user.services.UpdateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.throyer.example.api.shared.rest.Responses.ok;
import static com.github.throyer.example.api.utils.ID.decode;

@Slf4j
@Tag(name = "Users")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UpdateUserController {
  private final UpdateUserService service;
  
  @PutMapping("/{user_id}")
  @SecurityRequirement(name = "jwt")
  @PreAuthorize("hasAnyAuthority('USER')")
  @Operation(summary = "Update user data")
  public ResponseEntity<UserInformation> update(
    @PathVariable("user_id") String id,
    @RequestBody @Validated UpdateUserData data
  ) {
    var user = service.update(decode(id), data);
    return ok(new UserInformation(user));
  }
}
