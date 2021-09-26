package com.github.throyer.common.springboot.api.domain.models.entity;

import static com.github.throyer.common.springboot.api.utils.Random.code;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "recovery")
public class Recovery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "expires_in", nullable = false )
    private LocalDateTime expiresIn;

    @Column(name = "confirmed")
    private Boolean confirmed = false;

    @Column(name = "used")
    private Boolean used = false;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    public Recovery() { }

    public Recovery(User user, Integer minutesToExpire) {
        this.user = user;
        this.expiresIn = LocalDateTime.now().plusMinutes(minutesToExpire);
        this.code = code();
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

    public Boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Boolean isUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    public Boolean nonExpired() {
        return expiresIn.isAfter(LocalDateTime.now());
    }

}
