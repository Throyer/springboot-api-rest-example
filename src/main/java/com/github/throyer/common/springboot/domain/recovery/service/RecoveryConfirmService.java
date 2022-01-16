package com.github.throyer.common.springboot.domain.recovery.service;

import com.github.throyer.common.springboot.domain.recovery.repository.RecoveryRepository;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RecoveryConfirmService {

    @Autowired
    private UserRepository users;

    @Autowired
    private RecoveryRepository recoveryRepository;

    public void confirm(String email, String code) {
        var user = users.findOptionalByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        var recovery = recoveryRepository
                .findFirstOptionalByUser_IdAndConfirmedIsFalseAndUsedIsFalseOrderByExpiresInDesc(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (!recovery.nonExpired() || !recovery.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        recovery.setConfirmed(true);

        recoveryRepository.save(recovery);
    }
}
