package com.github.throyer.example.api.domain.passwordrecovery.controllers;

import com.github.throyer.example.api.domain.passwordrecovery.dtos.RecoveryUpdatePasswordRequestData;
import com.github.throyer.example.api.domain.passwordrecovery.services.UpdatePasswordWithRecoveryCodeAndEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Tag(name = "Password recovery")
@RequestMapping("/recoveries")
@AllArgsConstructor
public class UpdatePasswordWithRecoveryController {
  private final UpdatePasswordWithRecoveryCodeAndEmailService service;
  
  @PostMapping("/update")
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Update user password")
  public void update(@RequestBody RecoveryUpdatePasswordRequestData update) {
    service.update(update.getEmail(), update.getCode(), update.getPassword());
  }
}
