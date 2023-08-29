package com.github.throyer.example.api.shared.identity;

import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.identity.erros.IdentityEncoderException;
import org.hashids.Hashids;
import org.springframework.stereotype.Component;

import static com.github.throyer.example.api.infra.constants.SecurityConstants.HASH_LENGTH;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class IdentityEncoderWithHashIds implements IdentityEncoder {
  private final Hashids hashids;

  public IdentityEncoderWithHashIds(SecurityProperties properties) {
    this.hashids = new Hashids(properties.getHashidSecret(), HASH_LENGTH);;
  }
  
  @Override
  public String encode(Long id) {
    try {
      return this.hashids.encode(requireNonNull(id, "id is null"));
    } catch (Exception exception) {
      throw new IdentityEncoderException("error on id encoding", exception);
    }
  }

  @Override
  public Long decode(String id) {
    try {
      return stream(this.hashids.decode(requireNonNull(id, "id is null")))
        .boxed()
        .findFirst()
        .orElse(null);
    } catch (Exception exception) {
      throw new IdentityEncoderException("error on id encoding", exception);
    }
  }
}
