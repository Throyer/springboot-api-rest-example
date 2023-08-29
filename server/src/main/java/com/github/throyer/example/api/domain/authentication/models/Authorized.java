package com.github.throyer.example.api.domain.authentication.models;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Optional;

import static com.github.throyer.example.api.utils.ID.decode;
import static java.util.Optional.ofNullable;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

public class Authorized extends User {
  
  @Getter
  private final Long id;
  
  @Getter
  private final String name;

  public Authorized(String id, List<SimpleGrantedAuthority> authorities) {
    super("USERNAME", "SECRET", authorities);
    this.id = decode(id);
    this.name = "";
  }

  public Authorized(com.github.throyer.example.api.domain.user.persistence.models.User user) {
    super(
      user.getEmail(),
      user.getPassword(),
      user.isActive(),
      true,
      true,
      true,
      user.getRoles()
    );
    this.id = user.getId();
    this.name = user.getEmail();
  }

  public UsernamePasswordAuthenticationToken getAuthentication() {
    return new UsernamePasswordAuthenticationToken(this, null, getAuthorities());
  }

  public Boolean isAdmin() {
    return getAuthorities()
      .stream()
      .anyMatch((role) -> role.getAuthority().equals("ADM"));
  }

  public Boolean itsMeOrSessionIsADM(Long id) {
    var admin = isAdmin();
    var equals = getId().equals(id);
    if (admin) {
      return true;
    }
    return equals;
  }

  @Override
  public String toString() {
    return getId().toString();
  }

  public static Optional<Authorized> current() {
    return ofNullable(
      getContext().getAuthentication())
      .map(Authentication::getPrincipal)
      .map((principal) -> {
        if (principal instanceof Authorized authorized) {
          return authorized;
        }
        return null;
      });
  }
}
