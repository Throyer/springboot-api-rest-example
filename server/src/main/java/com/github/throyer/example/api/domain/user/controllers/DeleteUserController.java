package com.github.throyer.example.api.domain.user.controllers;

import com.github.throyer.example.api.domain.user.services.RemoveUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.github.throyer.example.api.utils.ID.decode;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
@Tag(name = "Users")
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class DeleteUserController {
  private final RemoveUserService service;
  
  @DeleteMapping("/{user_id}")
  @ResponseStatus(NO_CONTENT)
  @SecurityRequirement(name = "token")
  @Operation(summary = "Delete user")
  public void destroy(@PathVariable("user_id") String id) {
    service.remove(decode(id));
  }
}
