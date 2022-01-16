package com.github.throyer.common.springboot.domain.recovery.service;

import com.github.throyer.common.springboot.domain.recovery.repository.RecoveryRepository;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RecoveryUpdateService {

    @Autowired
    private UserRepository users;

    @Autowired
    private RecoveryRepository recoveries;

    public void update(String email, String code, String password) {
        var user = users.findOptionalByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        var recovery = recoveries
                .findFirstOptionalByUser_IdAndConfirmedIsTrueAndUsedIsFalseOrderByExpiresInDesc(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (!recovery.nonExpired() || !recovery.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        user.updatePassword(password);
        users.save(user);

        recovery.setUsed(true);
        recoveries.save(recovery);
    }
}
