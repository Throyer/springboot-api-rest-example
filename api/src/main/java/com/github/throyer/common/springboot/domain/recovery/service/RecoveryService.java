package com.github.throyer.common.springboot.domain.recovery.service;

import static com.github.throyer.common.springboot.constants.MAIL.ERROR_SENDING_EMAIL_MESSAGE_TO;
import static com.github.throyer.common.springboot.constants.PASSWORD_RECOVERY.MINUTES_TO_EXPIRE_RECOVERY_CODE;
import static com.github.throyer.common.springboot.constants.PASSWORD_RECOVERY.SUBJECT_PASSWORD_RECOVERY_CODE;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.common.springboot.domain.mail.service.MailService;
import com.github.throyer.common.springboot.domain.recovery.entity.Recovery;
import com.github.throyer.common.springboot.domain.recovery.model.RecoveryEmail;
import com.github.throyer.common.springboot.domain.recovery.repository.RecoveryRepository;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;

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
