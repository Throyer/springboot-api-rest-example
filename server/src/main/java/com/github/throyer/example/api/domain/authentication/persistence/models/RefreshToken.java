package com.github.throyer.example.api.domain.authentication.persistence.models;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static java.util.UUID.randomUUID;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "refresh_token")
@Table(name = "refresh_token")
public class RefreshToken implements Serializable {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;
  
  @Column(name = "code")
  private String code;
  
  @Column(name = "expires_at")
  private LocalDateTime expiresAt;
  
  @Column(name = "available")
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
    return this.expiresAt.isAfter(now());
  }
}
