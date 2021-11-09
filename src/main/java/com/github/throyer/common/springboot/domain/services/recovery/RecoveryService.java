package com.github.throyer.common.springboot.domain.services.recovery;

import com.github.throyer.common.springboot.domain.models.emails.RecoveryEmail;
import com.github.throyer.common.springboot.domain.models.entity.Recovery;
import com.github.throyer.common.springboot.domain.repositories.RecoveryRepository;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.domain.services.email.MailService;
import com.github.throyer.common.springboot.domain.services.user.dto.Codes;
import com.github.throyer.common.springboot.domain.services.user.dto.RecoveryRequest;
import com.github.throyer.common.springboot.utils.Toasts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

@Service
public class RecoveryService {

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
            return "app/recovery/index";
        }
        
        var email = recovery.getEmail();
        
        recovery(email);
        
        model.addAttribute("codes", new Codes(email));
        
        return "app/recovery/confirm";
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
}
