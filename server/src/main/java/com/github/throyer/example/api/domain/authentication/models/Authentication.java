package com.github.throyer.example.api.domain.authentication.models;

import java.time.LocalDateTime;

import com.github.throyer.example.api.domain.user.dtos.UserInformation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Getter
public class Authentication {
  @Schema(requiredMode = REQUIRED)
  private final UserInformation user;

  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", requiredMode = REQUIRED)
  private final String accessToken;

  @Schema(example = "d67befed-bfde-4de4-b7a0-72c9a92667e5", requiredMode = REQUIRED)
  private final String refreshToken;

  @Schema(example = "2022-10-01T17:06:42.130", requiredMode = REQUIRED)
  private final LocalDateTime expiresAt;

  public Authentication(
    UserInformation user,
    String accessToken,
    String refreshToken,
    LocalDateTime expiresAt
  ) {
    this.user = user;
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.expiresAt = expiresAt;
  }
}
