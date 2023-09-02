package com.github.throyer.example.api.domain.user.persistence.models;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.github.throyer.example.api.shared.persistence.repositories.Queries.NON_DELETED_CLAUSE;
import static com.github.throyer.example.api.utils.Passwords.encode;
import static com.github.throyer.example.api.utils.Passwords.matches;
import static jakarta.persistence.CascadeType.DETACH;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.NONE;

import java.util.List;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.throyer.example.api.domain.role.persistence.models.Role;
import com.github.throyer.example.api.shared.persistence.models.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "user")
@Table(name = "user")
@Where(clause = NON_DELETED_CLAUSE)
@NoArgsConstructor
public class User extends Auditable {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  
  @Getter(NONE)
  @Column(name = "active", nullable = false)
  private Boolean active = true;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "deleted_email")
  private String deletedEmail;

  @Column(name = "email_confirmed", unique = true)
  private Boolean emailConfirmed = false;

  @JsonProperty(access = WRITE_ONLY)
  @Column(name = "password", nullable = false)
  private String password;

  @ManyToMany(cascade = DETACH, fetch = LAZY)
  @JoinTable(name = "user_role",
    joinColumns = {@JoinColumn(name = "user_id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id")}
  )
  private List<Role> roles;

  public Boolean isActive() {
    return active;
  }

  public Boolean passwordMatches(String rawPassword) {
    return matches(rawPassword, this.password);
  }
  
  public Boolean emailHasConfirmed() {
    return emailConfirmed;
  }
  
  public Boolean hasSameEmail(User other) {
    return this.email.equalsIgnoreCase(requireNonNull(other.getEmail(), "user email is null"));
  }

  public List<String> getAuthorities() {
    return ofNullable(roles)
      .map(roles -> roles
        .stream()
        .map(Role::getAuthority)
        .toList())
      .orElseGet(List::of);
  }
  
  @PrePersist
  private void persist() {
    this.password = encode(this.password);
  }

  public User(Long id) {
    this.id = id;
  }

  public User(String name, String email, String password, Role role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.active = true;
    this.emailConfirmed = false;
    this.roles = List.of(requireNonNull(role, "role is null"));
  }

  public User(String name, String email, String password, List<Role> roles) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.active = true;
    this.emailConfirmed = false;
    this.roles = roles;
  }

  public User(
    Long id,
    String name,
    String email,
    String password,
    Boolean emailConfirmed,
    List<Role> roles
  ) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.emailConfirmed = emailConfirmed;
    this.active = true;
    this.roles = roles;
  }
}
