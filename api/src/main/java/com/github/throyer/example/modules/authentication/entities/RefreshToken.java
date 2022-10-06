package com.github.throyer.example.modules.authentication.entities;

import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.github.throyer.example.modules.users.entities.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String code;

    private LocalDateTime expiresAt;

    private Boolean available = true;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public RefreshToken(User user, Integer daysToExpire) {
        this.user = user;
        this.expiresAt = now().plusDays(daysToExpire);
        this.code = randomUUID().toString();
    }

    public Boolean nonExpired() {
        return expiresAt.isAfter(now());
    }
}
