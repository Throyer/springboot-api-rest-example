package com.github.throyer.example.modules.ssr.services;

import static com.github.throyer.example.modules.infra.constants.MailConstants.ERROR_SENDING_EMAIL_MESSAGE_TO;
import static com.github.throyer.example.modules.infra.environments.PasswordRecoveryEnvironments.MINUTES_TO_EXPIRE_RECOVERY_CODE;
import static com.github.throyer.example.modules.infra.environments.PasswordRecoveryEnvironments.SUBJECT_PASSWORD_RECOVERY_CODE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.throyer.example.modules.mail.services.MailService;
import com.github.throyer.example.modules.recoveries.dtos.RecoveryEmail;
import com.github.throyer.example.modules.recoveries.entities.Recovery;
import com.github.throyer.example.modules.recoveries.repositories.RecoveryRepository;
import com.github.throyer.example.modules.users.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AppRecoveryService {

  @Autowired
  private UserRepository users;

  @Autowired
  private RecoveryRepository recoveryRepository;

  @Autowired
  private MailService mailService;

  public void recovery(String email) {

    var user = users.findByEmail(email);

    if (user.isEmpty()) {
      return;
    }

    var recovery = new Recovery(user.get(), MINUTES_TO_EXPIRE_RECOVERY_CODE);

    recoveryRepository.save(recovery);

    var sendEmailInBackground = new Thread(() -> {
      var recoveryEmail = new RecoveryEmail(
        email,
        SUBJECT_PASSWORD_RECOVERY_CODE,
        user.get().getName(),
        recovery.getCode()
      );

      try {
        mailService.send(recoveryEmail);
      } catch (Exception exception) {
        log.info(ERROR_SENDING_EMAIL_MESSAGE_TO, email);
      }
    });

    sendEmailInBackground.start();
  }

  public void confirm(String email, String code) {
    var user = users.findByEmail(email)
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

    var recovery = recoveryRepository
      .findFirstOptionalByUser_IdAndConfirmedIsFalseAndUsedIsFalseOrderByExpiresAtDesc(user.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

    if (!recovery.nonExpired() || !recovery.getCode().equals(code)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    recovery.setConfirmed(true);

    recoveryRepository.save(recovery);
  }

  public void update(String email, String code, String password) {
    var user = users.findByEmail(email)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

    var recovery = recoveryRepository
      .findFirstOptionalByUser_IdAndConfirmedIsTrueAndUsedIsFalseOrderByExpiresAtDesc(user.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

    if (!recovery.nonExpired() || !recovery.getCode().equals(code)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    user.updatePassword(password);
    users.save(user);

    recovery.setUsed(true);
    recoveryRepository.save(recovery);
  }
}
