package com.github.throyer.common.springboot.domain.recovery.service;

import com.github.throyer.common.springboot.domain.recovery.model.RecoveryEmail;
import com.github.throyer.common.springboot.domain.recovery.entity.Recovery;
import com.github.throyer.common.springboot.domain.recovery.repository.RecoveryRepository;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import com.github.throyer.common.springboot.domain.mail.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecoveryService {
    
    private static final Integer MINUTES_TO_EXPIRE = 20;

    @Autowired
    private UserRepository users;

    @Autowired
    private RecoveryRepository recoveries;

    @Autowired
    private MailService service;
        
    public void recovery(String email) {
        var user = users.findOptionalByEmail(email);

        if (user.isEmpty()) {
            return;
        }

        var recovery = new Recovery(user.get(), MINUTES_TO_EXPIRE);

        recoveries.save(recovery);

        try {
            var recoveryEmail = new RecoveryEmail(
                email,
                "password recovery code",
                user.get().getName(),
                recovery.getCode()
            );
            
            service.send(recoveryEmail);
        } catch (Exception exception) { }
    }
}
