package com.github.throyer.example.api.domain.passwordrecovery.controllers;

import com.github.throyer.example.api.domain.passwordrecovery.dtos.RecoveryConfirmRequestData;
import com.github.throyer.example.api.domain.passwordrecovery.services.ConfirmRecoveryWithCodeAndEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Tag(name = "Password recovery")
@RequestMapping("/recoveries")
@AllArgsConstructor
public class ConfirmRecoveryCodeController {
  private final ConfirmRecoveryWithCodeAndEmailService service;
  
  @PostMapping("/confirm")
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Confirm recovery code")
  public void confirm(@RequestBody RecoveryConfirmRequestData data) {
    service.confirm(data.getEmail(), data.getCode());
  }
}
