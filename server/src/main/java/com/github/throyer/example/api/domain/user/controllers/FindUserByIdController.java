package com.github.throyer.example.api.domain.user.controllers;

import com.github.throyer.example.api.domain.user.dtos.UserInformation;
import com.github.throyer.example.api.domain.user.services.FindUserByIdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.github.throyer.example.api.shared.rest.Responses.ok;
import static com.github.throyer.example.api.utils.ID.decode;

@Slf4j
@Tag(name = "Users")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class FindUserByIdController {
  private final FindUserByIdService service;
  
  @GetMapping("/{user_id}")
  @SecurityRequirement(name = "token")
  @PreAuthorize("hasAnyAuthority('ADM', 'USER')")
  @Operation(summary = "Show user info")
  public ResponseEntity<UserInformation> show(@PathVariable("user_id") String id) {
    var user = service.find(decode(id));
    return ok(new UserInformation(user));
  }
}
