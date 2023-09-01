package com.github.throyer.example.api.domain.authentication.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAuthenticationWithRefreshToken {
  @Schema(example = "1767995b-7865-430f-9181-189704235ae7", requiredMode = REQUIRED)
  @NotEmpty(message = "${field.refresh.session.refresh-code.required}")
  private String refreshToken;
}
