package com.github.throyer.common.springboot.controllers.app;

import com.github.throyer.common.springboot.domain.services.recovery.RecoveryConfirmService;
import com.github.throyer.common.springboot.domain.services.recovery.RecoveryService;
import com.github.throyer.common.springboot.domain.services.recovery.RecoveryUpdateService;
import com.github.throyer.common.springboot.domain.services.user.dto.Codes;
import com.github.throyer.common.springboot.domain.services.user.dto.RecoveryRequest;
import com.github.throyer.common.springboot.domain.services.user.dto.Update;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        return "/app/recovery/index";
    }

    @PostMapping
    public String index(
        @Valid RecoveryRequest recovery,
        BindingResult result,
        Model model
    ) {
        return recoveryService.recovery(recovery, result, model);
    }

//    @GetMapping("/confirm")
//    public String confirm(Model model) {
//        model.addAttribute("codes", new Codes());
//        return "/app/recovery/confirm";
//    }

    @PostMapping("/confirm")
    public String confirm(
        @Valid Codes codes,
        BindingResult result,
        RedirectAttributes redirect,
        Model model
    ) {
        return confirmService.confirm(codes, result, model, redirect);
    }

//    @GetMapping("/update")
//    public String update(Model model) {
//        model.addAttribute("update", new Update());
//        return "/app/recovery/update";
//    }

    @PostMapping("/update")
    public String update(
        @Valid Update update,
        BindingResult result,
        RedirectAttributes redirect,
        Model model
    ) {
        return updateService.update(update, result, model, redirect);
    }
}
