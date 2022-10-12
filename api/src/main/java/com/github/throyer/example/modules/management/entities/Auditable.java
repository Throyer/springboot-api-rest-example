package com.github.throyer.example.modules.management.entities;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;
import static javax.persistence.FetchType.LAZY;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.throyer.example.modules.authentication.models.Authorized;
import com.github.throyer.example.modules.management.models.Entity;
import com.github.throyer.example.modules.users.entities.User;

import lombok.EqualsAndHashCode;

@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Auditable implements Entity {

  @Override
  @EqualsAndHashCode.Include
  public abstract Long getId();

  @JsonIgnore
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @JsonIgnore
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @JsonIgnore
  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @JoinColumn(name = "created_by")
  @ManyToOne(optional = true, fetch = LAZY)
  private User createdBy;

  @JoinColumn(name = "updated_by")
  @ManyToOne(optional = true, fetch = LAZY)
  private User updatedBy;

  @JoinColumn(name = "deleted_by")
  @ManyToOne(optional = true, fetch = LAZY)
  private User deletedBy;

  @JsonIgnore
  public Optional<User> getCreatedBy() {
    return ofNullable(createdBy);
  }

  @JsonIgnore
  public Optional<User> getUpdatedBy() {
    return ofNullable(updatedBy);
  }

  @JsonIgnore
  public Optional<User> getDeletedBy() {
    return ofNullable(deletedBy);
  }

  @Column(name = "active", nullable = false)
  private Boolean active = true;

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public Boolean isActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  @PrePersist
  private void save() {
    createdAt = now();
    createdBy = Authorized.current()
        .map(authorized -> new User(authorized.getId()))
        .orElse(null);
  }

  @PreUpdate
  private void update() {
    updatedAt = now();
    updatedBy = Authorized.current()
      .map(authorized -> new User(authorized.getId()))
        .orElse(null);
  }
}