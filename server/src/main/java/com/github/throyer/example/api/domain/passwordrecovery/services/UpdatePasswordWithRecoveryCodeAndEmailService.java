package com.github.throyer.example.api.domain.passwordrecovery.services;

import com.github.throyer.example.api.domain.passwordrecovery.persistence.repositories.RecoveryRepository;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.infra.constants.MessageConstants.Authentication.RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE;
import static com.github.throyer.example.api.shared.rest.Responses.forbidden;
import static com.github.throyer.example.api.utils.Passwords.encode;

@Service
@AllArgsConstructor
public class UpdatePasswordWithRecoveryCodeAndEmailService {
  private final UserRepository users;
  private final RecoveryRepository recoveries;
  private final Internationalization i18n;

  public void update(String email, String code, String password) {
    var user = users.findOptionalByEmail(email)
      .orElseThrow(() -> forbidden(i18n.message(RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE)));

    var recovery = recoveries
      .findFirstOptionalByUser_IdAndConfirmedIsTrueAndUsedIsFalseOrderByExpiresAtDesc(user.getId())
      .orElseThrow(() -> forbidden(i18n.message(RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE)));

    if (!recovery.nonExpired() || !recovery.getCode().equals(code)) {
      throw forbidden(i18n.message(RECOVERY_CODE_EXPIRED_OR_INVALID_MESSAGE));
    }

    user.setPassword(encode(password));
    users.save(user);

    recovery.setUsed(true);
    recoveries.save(recovery);
  }
}
