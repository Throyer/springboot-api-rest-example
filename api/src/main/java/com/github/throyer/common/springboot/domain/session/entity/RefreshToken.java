package com.github.throyer.common.springboot.domain.session.entity;

import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import java.util.Optional;
import static java.util.UUID.randomUUID;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class RefreshToken implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String code;

    private LocalDateTime expiresIn;

    private Boolean available = true;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public RefreshToken(User user, Integer daysToExpire) {
        this.user = user;
        this.expiresIn = now().plusDays(daysToExpire);
        this.code = randomUUID().toString();
    }

    public RefreshToken(UserDetails user, Integer daysToExpire) {
        this.expiresIn = now().plusDays(daysToExpire);
        this.code = randomUUID().toString();
        this.user = Optional.ofNullable(user.getId()).map(User::new).orElse(null);
    }

    public Boolean nonExpired() {
        return expiresIn.isAfter(now());
    }
}
