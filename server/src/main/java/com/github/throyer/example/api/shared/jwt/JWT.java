package com.github.throyer.example.api.shared.jwt;

import com.github.throyer.example.api.domain.authentication.models.Authorized;

import java.time.LocalDateTime;
import java.util.List;

public interface JWT {
  String encode(
    String id,
    List<String> roles,
    LocalDateTime expiresAt,
    String secret
  );

  Authorized decode(
    String token,
    String secret
  );
}
