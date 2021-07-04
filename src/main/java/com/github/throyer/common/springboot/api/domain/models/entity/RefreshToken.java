package com.github.throyer.common.springboot.api.domain.models.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expires_in", nullable = false )
    private LocalDateTime expiresIn;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public RefreshToken() { }

    public RefreshToken(User user, Integer daysToExpire) {
        this.user = user;
        this.expiresIn = LocalDateTime.now().plusDays(daysToExpire);
        this.code = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(LocalDateTime expiresIn) {
        this.expiresIn = expiresIn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean nonExpired() {
        return expiresIn.isAfter(LocalDateTime.now());
    }
}
