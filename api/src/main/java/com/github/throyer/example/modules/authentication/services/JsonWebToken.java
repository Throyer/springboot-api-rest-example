package com.github.throyer.example.modules.authentication.services;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static java.time.ZoneId.systemDefault;
import static java.util.Arrays.stream;
import static java.util.Date.from;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.github.throyer.example.modules.authentication.models.Authorized;
import com.github.throyer.example.modules.infra.environments.SecurityEnvironments;

import io.jsonwebtoken.Jwts;

public class JsonWebToken {
  public String encode(
      String id,
      List<String> roles,
      LocalDateTime expiration,
      String secret
    ) {
    return Jwts.builder()
      .setSubject(id)
      .claim(SecurityEnvironments.ROLES_KEY_ON_JWT, String.join(",", roles))
      .setExpiration(from(expiration.atZone(systemDefault()).toInstant()))
      .signWith(HS256, secret)
      .compact();
  }

  public Authorized decode(String token, String secret) {
    var decoded = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

    var id = decoded.getBody().getSubject();

    var joinedRolesString = decoded.getBody().get(SecurityEnvironments.ROLES_KEY_ON_JWT).toString();
    var roles = joinedRolesString.split(",");
    var authorities = stream(roles).map(SimpleGrantedAuthority::new).toList();

    return new Authorized(id, authorities);
  }
}
