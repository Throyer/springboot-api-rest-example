package com.github.throyer.common.springboot.controllers.app;

import static com.github.throyer.common.springboot.utils.Responses.validate;

import com.github.throyer.common.springboot.domain.recovery.service.RecoveryConfirmService;
import com.github.throyer.common.springboot.domain.recovery.service.RecoveryService;
import com.github.throyer.common.springboot.domain.recovery.service.RecoveryUpdateService;
import com.github.throyer.common.springboot.domain.recovery.model.Codes;
import com.github.throyer.common.springboot.domain.recovery.model.RecoveryRequest;
import com.github.throyer.common.springboot.domain.recovery.model.Update;
import com.github.throyer.common.springboot.domain.shared.Type;
import com.github.throyer.common.springboot.utils.Toasts;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/app/recovery")
public class RecoveryController {

    @Autowired
    private RecoveryService recoveryService;

    @Autowired
    private RecoveryConfirmService confirmService;

    @Autowired
    private RecoveryUpdateService updateService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("recovery", new RecoveryRequest());
        return "app/recovery/index";
    }

    @PostMapping
    public String index(
        @Valid RecoveryRequest recovery,
        BindingResult result,
        Model model
    ) {

        if (validate(model, recovery, "recovery", result)) {
            return "app/recovery/index";
        }

        var email = recovery.getEmail();

        recoveryService.recovery(email);

        model.addAttribute("codes", new Codes(email));

        return "app/recovery/confirm";
    }

    @PostMapping("/confirm")
    public String confirm(
        @Valid Codes codes,
        BindingResult result,
        RedirectAttributes redirect,
        Model model
    ) {

        if (validate(model, codes, "recovery", result)) {
            return "app/recovery/confirm";
        }

        try {
            confirmService.confirm(codes.getEmail(), codes.code());
        } catch (ResponseStatusException exception) {

            Toasts.add(model, "Código expirado ou invalido.", Type.DANGER);
            model.addAttribute("confirm", codes);
            return "app/recovery/confirm";
        }

        model.addAttribute("update", new Update(codes));

        return "app/recovery/update";
    }

    @PostMapping("/update")
    public String update(
        @Valid Update update,
        BindingResult result,
        RedirectAttributes redirect,
        Model model
    ) {
        update.validate(result);

        if (validate(model, update, "update", result)) {
            return "app/recovery/update";
        }

        try {
            updateService.update(update.getEmail(), update.code(), update.getPassword());
        } catch (ResponseStatusException exception) {
            Toasts.add(model, "Código expirado ou invalido.", Type.DANGER);
            model.addAttribute("update", update);
            return "app/recovery/update";
        }

        Toasts.add(redirect, "Sua senha foi atualizada com sucesso.", Type.SUCCESS);
        return "redirect:/app/login";
    }
}
