package com.github.throyer.common.springboot.api.domain.models.entity;

import static com.github.throyer.common.springboot.api.domain.services.security.SecurityService.authorized;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.throyer.common.springboot.api.domain.models.shared.Entity;

@MappedSuperclass
public abstract class BasicEntity implements Entity {

    public static final String NON_DELETED_CLAUSE = "deleted_at IS NULL";
    
    public static final String SET_ALL_DELETED_SQL = """
        UPDATE
            #{#entityName}
        SET
            deleted_at = CURRENT_TIMESTAMP
    """;

    public static final String SET_DELETED_SQL = SET_ALL_DELETED_SQL + "WHERE id = ?1";

    @Override
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
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User createdBy;
    
    @JoinColumn(name = "updated_by")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User updatedBy;
    
    @JoinColumn(name = "deleted_by")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private User deletedBy;

    @JsonIgnore
    public Optional<User> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @JsonIgnore
    public Optional<User> getUpdatedBy() {
        return Optional.ofNullable(updatedBy);
    }

    @JsonIgnore
    public Optional<User> getDeletedBy() {
        return Optional.ofNullable(deletedBy);
    }

    @Column(name = "active", nullable = false)
    private Boolean ativo = true;

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
        return this.ativo;
    }

    public void setActive(Boolean active) {
        this.ativo = active;
    }

    @PrePersist
    private void save() {
        createdAt = LocalDateTime.now();
        createdBy = authorized()
        .map(authorized -> new User(authorized.getId()))
            .orElse(null);
    }

    @PreUpdate
    private void update() {
        updatedAt = LocalDateTime.now();
        updatedBy = authorized()
        .map(authorized -> new User(authorized.getId()))
            .orElse(null);
    }
}