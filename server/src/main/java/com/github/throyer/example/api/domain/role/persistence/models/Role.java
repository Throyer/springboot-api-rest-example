package com.github.throyer.example.api.domain.role.persistence.models;

import static com.github.throyer.example.api.shared.persistence.repositories.Queries.NON_DELETED_CLAUSE;
import static jakarta.persistence.GenerationType.IDENTITY;

import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import com.github.throyer.example.api.shared.persistence.models.Auditable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "role")
@Table(name = "role")
@Where(clause = NON_DELETED_CLAUSE)
@NoArgsConstructor
public class Role extends Auditable implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "name", unique = true, length = 100)
  private String name;

  @Column(name = "deleted_name")
  private String deletedName;

  @Column(name = "short_name", unique = true)
  private String shortName;

  @Column(name = "deleted_short_name")
  private String deletedShortName;

  @Column(unique = true)
  private String description;

  @Column(name = "active", nullable = false)
  private Boolean active = true;

  @Override
  public String getAuthority() {
    return this.getShortName();
  }

  public Role(String name) {
    this.name = name;
  }
}
