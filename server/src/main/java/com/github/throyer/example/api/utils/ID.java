package com.github.throyer.example.api.utils;

import com.github.throyer.example.api.shared.identity.IdentityEncoder;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
public class ID {
  private static IdentityEncoder encoder;
  public ID(IdentityEncoder encoder) {
    ID.encoder = encoder;
  }

  public static String encode(Long id) {
    return requireNonNull(encoder, "string id encoder is null")
      .encode(id);
  }

  public static Long decode(String id) {
    return requireNonNull(encoder, "string id encoder is null")
      .decode(id);
  }
}
