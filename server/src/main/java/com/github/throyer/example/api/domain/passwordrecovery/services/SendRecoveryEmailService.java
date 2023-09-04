package com.github.throyer.example.api.domain.passwordrecovery.services;

import com.github.throyer.example.api.domain.passwordrecovery.models.RecoveryEMail;
import com.github.throyer.example.api.domain.passwordrecovery.persistence.models.Recovery;
import com.github.throyer.example.api.domain.passwordrecovery.persistence.repositories.RecoveryRepository;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.infra.environments.RecoveryProperties;
import com.github.throyer.example.api.infra.mail.services.EMailService;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.infra.constants.MessageConstants.Authentication.EMAIL_WAS_NOT_CONFIRMED_MESSAGE;
import static com.github.throyer.example.api.infra.constants.MessageConstants.Recovery.EMAIL_SUBJECT_MESSAGE;
import static com.github.throyer.example.api.shared.rest.Responses.forbidden;

@Service
@AllArgsConstructor
public class SendRecoveryEmailService {
  private final RecoveryProperties properties;
  private final UserRepository userRepository;
  private final RecoveryRepository recoveryRepository;
  private final EMailService eMailService;
  private final Internationalization i18n;

  public void sendTo(String email) {
    userRepository.findOptionalByEmail(email)
      .ifPresent(user -> {

        if (!user.emailHasConfirmed()) {
          throw forbidden(i18n.message(EMAIL_WAS_NOT_CONFIRMED_MESSAGE));
        }
        
        var recovery = new Recovery(user, properties.getMinutesToExpire());

        recoveryRepository.save(recovery);

        var recoveryEmail = new RecoveryEMail(
          email,
          i18n.message(EMAIL_SUBJECT_MESSAGE),
          user.getName(),
          recovery.getCode()
        );

        eMailService.send(recoveryEmail);
      });
  }
}
