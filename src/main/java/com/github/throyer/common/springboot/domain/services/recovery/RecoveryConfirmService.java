package com.github.throyer.common.springboot.domain.services.recovery;

import com.github.throyer.common.springboot.domain.models.shared.Type;
import com.github.throyer.common.springboot.domain.repositories.RecoveryRepository;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.domain.services.user.dto.Codes;
import com.github.throyer.common.springboot.domain.services.user.dto.Update;
import com.github.throyer.common.springboot.utils.Toasts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class RecoveryConfirmService {

    @Autowired
    private UserRepository users;

    @Autowired
    private RecoveryRepository recoveries;

    public String confirm(Codes codes, BindingResult result, Model model, RedirectAttributes redirect) {
        
        if (result.hasErrors()) {
            Toasts.add(model, result);
            model.addAttribute("confirm", codes);
            return "app/recovery/confirm";
        }

        try {
            
            confirm(codes.getEmail(), codes.code());
            return "redirect:/app/recovery/update";
            
        } catch (ResponseStatusException exception) {
            
            if (exception.getStatus().equals(HttpStatus.FORBIDDEN)) {
                
                Toasts.add(model, "Código expirado ou invalido.", Type.DANGER);
                model.addAttribute("confirm", codes);
                
                return "app/recovery/confirm";
            }
            
            model.addAttribute("update", new Update(codes));
            
            return "app/recovery/update";
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
}
