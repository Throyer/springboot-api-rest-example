package com.github.throyer.example.api.shared.jwt;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.List;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.time.ZoneId.systemDefault;
import static java.util.Arrays.stream;
import static java.util.Date.from;

public class JsonWebTokenImplementation implements JWT {
  private static final String ROLES_KEY_ON_JWT = "roles";
  @Override
  public String encode(
    String id,
    List<String> roles,
    LocalDateTime expiresAt,
    String secret
  ) {
    return Jwts.builder()
      .setSubject(id)
      .claim(ROLES_KEY_ON_JWT, String.join(",", roles))
      .setExpiration(from(expiresAt.atZone(systemDefault()).toInstant()))
      .signWith(HS256, secret)
      .compact();
  }

  @Override
  public Authorized decode(String token, String secret) {
    var decoded = Jwts
      .parser()
      .setSigningKey(secret)
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
