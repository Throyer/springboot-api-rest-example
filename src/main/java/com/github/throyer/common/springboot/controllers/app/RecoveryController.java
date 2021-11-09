package com.github.throyer.common.springboot.controllers.app;

import com.github.throyer.common.springboot.domain.services.user.RecoveryPasswordService;
import com.github.throyer.common.springboot.domain.services.user.dto.Codes;
import com.github.throyer.common.springboot.domain.services.user.dto.RecoveryRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/recovery")
public class RecoveryController {
    
    @Autowired
    private RecoveryPasswordService service;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("recovery", new RecoveryRequest());
        return "/app/recovery/index";
    }

    @PostMapping
    public String create(
        @Valid RecoveryRequest recovery,
        BindingResult result,
        Model model
    ) {
        return service.recovery(recovery, result, model);
    }

    @GetMapping("/confirm")
    public String codes(Model model) {
        model.addAttribute("codes", new Codes());
        return "/app/recovery/confirm";
    }

    @PostMapping("/confirm")
    public String confirm(
        @Valid Codes codes,
        BindingResult result,
        Model model
    ) {
        return service.confirm(codes, result, model);
    }
}
