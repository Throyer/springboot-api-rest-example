package com.github.throyer.example.modules.recoveries.controllers;

import static org.springframework.http.HttpStatus.NO_CONTENT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.github.throyer.example.modules.recoveries.dtos.RecoveryConfirm;
import com.github.throyer.example.modules.recoveries.dtos.RecoveryRequest;
import com.github.throyer.example.modules.recoveries.dtos.RecoveryUpdate;
import com.github.throyer.example.modules.recoveries.services.RecoveryConfirmService;
import com.github.throyer.example.modules.recoveries.services.RecoveryService;
import com.github.throyer.example.modules.recoveries.services.RecoveryUpdateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Password recovery")
@RequestMapping("/api/recoveries")
public class RecoveriesController {

  private final RecoveryService recoveryService;
  private final RecoveryConfirmService confirmService;
  private final RecoveryUpdateService updateService;

  @Autowired
  public RecoveriesController(
    RecoveryService recoveryService,
    RecoveryConfirmService confirmService,
    RecoveryUpdateService updateService
  ) {
    this.recoveryService = recoveryService;
    this.confirmService = confirmService;
    this.updateService = updateService;
  }

  @PostMapping
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Starts recovery password process", description = "Sends a email to user with recovery code")
  public void recovery(@RequestBody RecoveryRequest request) {
    recoveryService.recovery(request.getEmail());
  }

  @PostMapping("/confirm")
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Confirm recovery code")
  public void confirm(@RequestBody RecoveryConfirm confirm) {
    confirmService.confirm(confirm.getEmail(), confirm.getCode());
  }

  @PostMapping("/update")
  @ResponseStatus(NO_CONTENT)
  @Operation(summary = "Update user password")
  public void update(@RequestBody RecoveryUpdate update) {
    updateService.update(update.getEmail(), update.getCode(), update.getPassword());
  }
}
