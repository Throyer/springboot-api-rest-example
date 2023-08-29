package com.github.throyer.example.api.shared.persistence.models;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Getter
@Setter
public abstract class Auditable extends BaseEntity {
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @JoinColumn(name = "created_by")
  @ManyToOne(fetch = LAZY)
  private User createdBy;

  @JoinColumn(name = "updated_by")
  @ManyToOne(fetch = LAZY)
  private User updatedBy;

  @JoinColumn(name = "deleted_by")
  @ManyToOne(fetch = LAZY)
  private User deletedBy;
}
