package com.github.throyer.example.api.domain.passwordrecovery.services;

import com.github.throyer.example.api.domain.passwordrecovery.persistence.repositories.RecoveryRepository;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.infra.constants.MessageConstants.Recovery.INVALID_OR_EXPIRED_CODE_MESSAGE;
import static com.github.throyer.example.api.shared.rest.Responses.forbidden;

@Service
@AllArgsConstructor
public class ConfirmRecoveryWithCodeAndEmailService {
  private final UserRepository users;
  private final RecoveryRepository recoveryRepository;
  private final Internationalization i18n;

  public void confirm(String email, String code) {
    var user = users.findOptionalByEmail(email)
      .orElseThrow(() -> forbidden(i18n.message(INVALID_OR_EXPIRED_CODE_MESSAGE)));
    
    var recovery = recoveryRepository
      .findFirstOptionalByUser_IdAndConfirmedIsFalseAndUsedIsFalseOrderByExpiresAtDesc(user.getId())
      .orElseThrow(() -> forbidden(i18n.message(INVALID_OR_EXPIRED_CODE_MESSAGE)));

    if (!recovery.nonExpired() || !recovery.getCode().equals(code)) {
      throw forbidden(i18n.message(INVALID_OR_EXPIRED_CODE_MESSAGE));
    }

    recovery.setConfirmed(true);

    recoveryRepository.save(recovery);
  }
}
