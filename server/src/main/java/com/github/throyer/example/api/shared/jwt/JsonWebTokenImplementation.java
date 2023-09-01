package com.github.throyer.example.api.shared.jwt;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static io.jsonwebtoken.Jwts.builder;
import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.io.Decoders.BASE64;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.lang.String.join;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.ZoneId.systemDefault;
import static java.util.Arrays.stream;
import static java.util.Date.from;

@Slf4j
public class JsonWebTokenImplementation implements JWT {
  private static final String ROLES_KEY_ON_JWT = "roles";
  
  @Override
  public String encode(
    String id,
    List<String> roles,
    LocalDateTime expiresAt,
    String secret
  ) {
    return builder()
      .setSubject(id)
      .claim(ROLES_KEY_ON_JWT, join(",", roles))
      .setExpiration(from(expiresAt.atZone(systemDefault()).toInstant()))
      .signWith(secretToKey(secret))
      .compact();
  }
  
  private SecretKey secretToKey(String secret) {
    var bytes = secret.getBytes(UTF_8);    
    try {
      return hmacShaKeyFor(bytes);  
    } catch (WeakKeyException exception) {
      log.warn(exception.getMessage());
      return hmacShaKeyFor(Arrays.copyOf(bytes, 64));
    }
  }

  @Override
  public Authorized decode(String token, String secret) {
    var decoded = Jwts
      .parserBuilder()
      .setSigningKey(secretToKey(secret))
        .build()
          .parseClaimsJws(token);

    var id = decoded
      .getBody()
      .getSubject();

    var joinedRolesString = decoded
      .getBody()
      .get(ROLES_KEY_ON_JWT)
        .toString();
    
    var roles = joinedRolesString.split(",");
    
    var authorities = stream(roles)
      .map(SimpleGrantedAuthority::new)
        .toList();

    return new Authorized(id, authorities);
  }
}
