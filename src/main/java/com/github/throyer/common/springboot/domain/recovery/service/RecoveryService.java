package com.github.throyer.common.springboot.domain.recovery.service;

import com.github.throyer.common.springboot.domain.mail.service.MailService;
import com.github.throyer.common.springboot.domain.recovery.entity.Recovery;
import com.github.throyer.common.springboot.domain.recovery.model.RecoveryEmail;
import com.github.throyer.common.springboot.domain.recovery.repository.RecoveryRepository;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.github.throyer.common.springboot.utils.Constants.MAIL.*;
import static java.lang.String.format;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.WARNING;

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

        var user = users.findOptionalByEmail(email);

        if (user.isEmpty()) {
            return;
        }

        var recovery = new Recovery(user.get(), MINUTES_TO_EXPIRE_RECOVERY_CODE);

        recoveries.save(recovery);

        var sendEmailBackground = new Thread(() -> {
            try {
                var recoveryEmail = new RecoveryEmail(
                    email,
                    SUBJECT_PASSWORD_RECOVERY_CODE,
                    user.get().getName(),
                    recovery.getCode()
                );

                service.send(recoveryEmail);

                LOGGER.log(INFO, format(EMAIL_SENT_SUCCESSFULLY_MESSAGE_LOG_TEMPLATE, email));
            } catch (Exception exception) {
                LOGGER.log(WARNING, format(UNABLE_TO_SEND_EMAIL_MESSAGE_TEMPLATE, email), exception);
            }
        });

        sendEmailBackground.start();
    }
}
