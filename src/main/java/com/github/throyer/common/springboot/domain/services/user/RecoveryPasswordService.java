package com.github.throyer.common.springboot.domain.services.user;

import com.github.throyer.common.springboot.domain.models.emails.RecoveryEmail;
import com.github.throyer.common.springboot.domain.models.entity.Recovery;
import com.github.throyer.common.springboot.domain.repositories.RecoveryRepository;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.domain.services.email.MailService;
import com.github.throyer.common.springboot.domain.services.user.dto.Codes;
import com.github.throyer.common.springboot.domain.services.user.dto.RecoveryRequest;
import com.github.throyer.common.springboot.utils.Toasts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RecoveryPasswordService {

    @Autowired
    private UserRepository users;

    @Autowired
    private RecoveryRepository recoveries;

    @Autowired
    private MailService service;

    public String recovery(RecoveryRequest recovery, BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            Toasts.add(model, result);
            model.addAttribute("recovery", recovery);
            return "/app/recovery/index";
        }
        
        recovery(recovery.getEmail());
        
        return "redirect:/app/recovery/confirm";
    }
    
    public void recovery(String email) {
        var user = users.findOptionalByEmail(email);

        if (user.isEmpty()) {
            return;
        }

        var minutesToExpire = 20;

        var recovery = new Recovery(user.get(), minutesToExpire);

        recoveries.save(recovery);

        try {
            var recoveryEmail = new RecoveryEmail(email, "password recovery code", recovery.getCode());
            service.send(recoveryEmail);
        } catch (Exception exception) { }
    }
    
    public String confirm(Codes codes, BindingResult result, Model model) {
        if (result.hasErrors()) {
            Toasts.add(model, result);
            model.addAttribute("recovery", codes);
            return "/app/recovery/index";
        }
        
        try {
            confirm(codes.getEmail(), codes.code());
            return "redirect:/app/recovery/update";
        } catch (Exception exception) {
            return "redirect:/app/recovery/update";
        }
    }

    public void confirm(String email, String code) {
        var user = users.findOptionalByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        var recovery = recoveries
            .findFirstOptionalByUser_IdAndConfirmedIsFalseAndUsedIsFalseOrderByExpiresInDesc(user.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));

        if (!recovery.nonExpired() || !recovery.getCode().equals(code)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        recovery.setConfirmed(true);

        recoveries.save(recovery);

        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

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
