package com.github.throyer.example.modules.users.entities;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;
import static com.github.throyer.example.modules.infra.environments.SecurityEnvironments.ENCODER;
import static com.github.throyer.example.modules.management.repositories.Queries.NON_DELETED_CLAUSE;
import static com.github.throyer.example.modules.shared.utils.JSON.stringify;
import static java.util.Optional.ofNullable;
import static java.util.stream.Stream.of;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Tuple;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.throyer.example.modules.mail.models.Addressable;
import com.github.throyer.example.modules.management.entities.Auditable;
import com.github.throyer.example.modules.roles.entities.Role;
import com.github.throyer.example.modules.users.dtos.UpdateUserProps;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user")
@Where(clause = NON_DELETED_CLAUSE)
public class User extends Auditable implements Serializable, Addressable {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", unique = true)
  private String email;

  @JsonIgnore
  @Column(name = "deleted_email")
  private String deletedEmail;

  @JsonProperty(access = WRITE_ONLY)
  @Column(name = "password", nullable = false)
  private String password;

  @ManyToMany(cascade = DETACH, fetch = LAZY)
  @JoinTable(name = "user_role", joinColumns = {
      @JoinColumn(name = "user_id") }, inverseJoinColumns = {
          @JoinColumn(name = "role_id") })
  private List<Role> roles;

  public User() {
  }

  public User(Long id) {
    this.id = id;
  }

  public User(String name, String email, String password, List<Role> roles) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setId(BigInteger id) {
    this.id = ofNullable(id)
        .map(BigInteger::longValue)
        .orElse(null);
  }

  public List<String> getAuthorities() {
    return ofNullable(roles)
        .map(roles -> roles
            .stream()
            .map(Role::getAuthority)
            .toList())
        .orElseGet(() -> List.of());
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public String getEmail() {
    return email;
  }

  public void merge(UpdateUserProps props) {
    this.name = props.getName();
    this.email = props.getEmail();
  }

  public void updatePassword(String newPassword) {
    this.password = ENCODER.encode(newPassword);
  }

  public Boolean validatePassword(String password) {
    return ENCODER.matches(password, this.password);
  }

  @PrePersist
  private void created() {
    this.password = ENCODER.encode(password);
  }

  @Override
  public String toString() {
    return stringify(Map.of(
        "name", ofNullable(this.name).orElse(""),
        "email", ofNullable(this.email).orElse("")));
  }

  public static User from(Tuple tuple) {
    var user = new User();
    user.setId(tuple.get("id", BigInteger.class));
    user.setName(tuple.get("name", String.class));
    user.setActive(tuple.get("active", Boolean.class));
    user.setEmail(tuple.get("email", String.class));
    user.setPassword(tuple.get("password", String.class));
    user.setRoles(ofNullable(tuple.get("roles", String.class))
        .map(roles -> of(roles.split(","))
            .map(Role::new).toList())
        .orElse(List.of()));
    return user;
  }
}