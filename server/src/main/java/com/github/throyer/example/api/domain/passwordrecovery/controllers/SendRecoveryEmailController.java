package com.github.throyer.example.api.domain.passwordrecovery.controllers;

import com.github.throyer.example.api.domain.passwordrecovery.dtos.RecoveryRequestData;
import com.github.throyer.example.api.domain.passwordrecovery.services.SendRecoveryEmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@Tag(name = "Password recovery")
@RequestMapping("/recoveries")
@AllArgsConstructor
public class SendRecoveryEmailController {
  private final SendRecoveryEmailService service;

  @PostMapping
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Starts recovery password process", description = "Sends a email to user with recovery code")
  public void recovery(@RequestBody RecoveryRequestData request) {
    service.sendTo(request.getEmail());
  }
}
