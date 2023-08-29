package com.github.throyer.example.api.domain.user.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public interface CreateUserConflictResponse {
  @Schema(example = "e-mail unavailable", requiredMode = REQUIRED)
  String getMessage();

  @Schema(example = "409", requiredMode = REQUIRED)
  Integer getStatus();
}
