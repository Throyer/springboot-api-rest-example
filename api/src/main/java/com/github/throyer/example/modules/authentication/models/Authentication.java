package com.github.throyer.example.modules.authentication.models;

import java.time.LocalDateTime;

import com.github.throyer.example.modules.users.dtos.UserInformation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class Authentication {
  @Schema(required = true)
  private final UserInformation user;

  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c", required = true)
  private final String accessToken;

  @Schema(example = "d67befed-bfde-4de4-b7a0-72c9a92667e5", required = true)
  private final String refreshToken;

  @Schema(example = "2022-10-01T17:06:42.130", required =  true)
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
