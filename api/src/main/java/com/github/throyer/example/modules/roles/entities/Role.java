package com.github.throyer.example.modules.roles.entities;

import static com.github.throyer.example.modules.management.repositories.Queries.NON_DELETED_CLAUSE;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.throyer.example.modules.management.entities.Auditable;

import lombok.Getter;

@Getter
@Entity
@Table(name = "role")
@Where(clause = NON_DELETED_CLAUSE)
public class Role extends Auditable implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "name", nullable = true, unique = true)
  private String name;

  @JsonIgnore
  @Column(name = "deleted_name")
  private String deletedName;

  @Column(name = "short_name", nullable = true, unique = true)
  private String shortName;

  @JsonIgnore
  @Column(name = "deleted_short_name")
  private String deletedShortName;

  @Column(nullable = true, unique = true)
  private String description;

  public Role() { }

  public Role(String shortName) {
    this.shortName = shortName;
  }

  public Role(Long id) {
    this.id = id;
  }

  public Role(Long id, String shortName) {
    this.id = id;
    this.shortName = shortName;
  }

  @Override
  public String toString() {
    return this.getAuthority();
  }

  @JsonIgnore
  @Override
  public String getAuthority() {
    return this.getShortName();
  }
}