package com.github.throyer.example.modules.recoveries.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.github.throyer.example.modules.recoveries.repositories.RecoveryRepository;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class RecoveryConfirmService {

    @Autowired
    private UserRepository users;

    @Autowired
    private RecoveryRepository recoveryRepository;

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
}
