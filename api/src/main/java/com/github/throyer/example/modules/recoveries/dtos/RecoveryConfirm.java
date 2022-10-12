package com.github.throyer.example.modules.recoveries.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
public class RecoveryConfirm {

  @Schema(example = "jubileu@email.com")
  @Email(message = "{recovery.email.is-valid}")
  @NotEmpty(message = "{recovery.email.not-empty}")
  private String email;

  @Schema(example = "5894")
  @NotEmpty(message = "{recovery.code.not-empty}")
  private String code;
}
