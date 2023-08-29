package com.github.throyer.example.api.shared.identity;

public interface IdentityEncoder {
  String encode(Long id);
  Long decode(String id);
}
