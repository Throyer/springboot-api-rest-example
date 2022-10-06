package com.github.throyer.example.modules.authentication.dtos;

import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAuthenticationWithRefreshToken {
  @Schema(example = "1767995b-7865-430f-9181-189704235ae7", required = true)
  @NotEmpty(message = "o campo refreshToken é obrigatório")
  private String refreshToken;
}
