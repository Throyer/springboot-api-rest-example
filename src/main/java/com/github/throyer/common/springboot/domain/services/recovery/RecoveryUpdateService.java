package com.github.throyer.common.springboot.domain.services.recovery;

import com.github.throyer.common.springboot.domain.models.shared.Type;
import com.github.throyer.common.springboot.domain.repositories.RecoveryRepository;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;
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
public class RecoveryUpdateService {

    @Autowired
    private UserRepository users;

    @Autowired
    private RecoveryRepository recoveries;

    public String update(
        Update update,
        BindingResult result,
        Model model,
        RedirectAttributes redirect
    ) {
        
        update.validate(result);
        
        if (result.hasErrors()) {
            Toasts.add(model, result);
            model.addAttribute("update", update);
            return "/app/recovery/update";
        }

        try {
            update(update.getEmail(), update.code(), update.getPassword());
            return "redirect:/app/login";
        } catch (ResponseStatusException exception) {
            if (exception.getStatus().equals(HttpStatus.FORBIDDEN)) {
                Toasts.add(model, "Código expirado ou invalido.", Type.DANGER);
                model.addAttribute("update", update);
                return "/app/recovery/update";
            }
            
            Toasts.add(redirect, "Sua senha foi atualizada com sucesso.", Type.SUCCESS);
            return "redirect:/app/login";
        }
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
