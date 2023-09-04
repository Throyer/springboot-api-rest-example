package com.github.throyer.example.api.domain.passwordrecovery.persistence.models;

import com.github.throyer.example.api.domain.user.persistence.models.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import static com.github.throyer.example.api.utils.Random.code;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

@Setter
@Getter
@Entity(name = "recovery")
@Table(name = "recovery")
@NoArgsConstructor
public class Recovery {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @Getter(NONE)
  @Column(name = "confirmed")
  private Boolean confirmed = false;

  @Getter(NONE)
  @Column(name = "used")
  private Boolean used = false;

  @ManyToOne
  @JoinColumn(name = "user_id")  
  private User user;
  
  public Recovery(User user, Integer minutesToExpire) {
    this.user = user;
    this.expiresAt = LocalDateTime.now().plusMinutes(minutesToExpire);
    this.code = code();
  }

  public Boolean nonExpired() {
    return expiresAt.isAfter(LocalDateTime.now());
  }

  public Boolean isConfirmed() {
    return confirmed;
  }

  public Boolean isUsed() {
    return used;
  }
}
