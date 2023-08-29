package com.github.throyer.example.api.infra.handlers.swagger;

import io.swagger.v3.oas.annotations.media.Schema;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

public interface RequestWithoutAuthorizationResponse {
  @Schema(example = "Can't find bearer token on Authorization header.", requiredMode = REQUIRED)
  String getMessage();

  @Schema(example = "403", requiredMode = REQUIRED)
  Integer getStatus();
}

