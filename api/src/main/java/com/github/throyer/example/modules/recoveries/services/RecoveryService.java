package com.github.throyer.example.modules.recoveries.services;

import static com.github.throyer.example.modules.infra.constants.MailConstants.ERROR_SENDING_EMAIL_MESSAGE_TO;
import static com.github.throyer.example.modules.infra.environments.PasswordRecoveryEnvironments.MINUTES_TO_EXPIRE_RECOVERY_CODE;
import static com.github.throyer.example.modules.infra.environments.PasswordRecoveryEnvironments.SUBJECT_PASSWORD_RECOVERY_CODE;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.example.modules.mail.services.MailService;
import com.github.throyer.example.modules.recoveries.dtos.RecoveryEmail;
import com.github.throyer.example.modules.recoveries.entities.Recovery;
import com.github.throyer.example.modules.recoveries.repositories.RecoveryRepository;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class RecoveryService {

    private static final Logger LOGGER = Logger.getLogger(RecoveryService.class.getName());

    private final UserRepository users;
    private final RecoveryRepository recoveries;
    private final MailService service;

    @Autowired
    public RecoveryService(
        UserRepository users,
        RecoveryRepository recoveries,
        MailService service
    ) {
        this.users = users;
        this.recoveries = recoveries;
        this.service = service;
    }

    public void recovery(String email) {

        var user = users.findByEmail(email);

        if (user.isEmpty()) {
            return;
        }

        var recovery = new Recovery(user.get(), MINUTES_TO_EXPIRE_RECOVERY_CODE);

        recoveries.save(recovery);

        var sendEmailInBackground = new Thread(() -> {
            var recoveryEmail = new RecoveryEmail(
                email,
                SUBJECT_PASSWORD_RECOVERY_CODE,
                user.get().getName(),
                recovery.getCode()
            );

            try {                
                service.send(recoveryEmail);
            } catch (Exception exception) {
                LOGGER.log(Level.INFO, ERROR_SENDING_EMAIL_MESSAGE_TO, email);
            }
        });

        sendEmailInBackground.start();
    }
}
