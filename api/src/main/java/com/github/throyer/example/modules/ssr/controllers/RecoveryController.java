package com.github.throyer.example.modules.ssr.controllers;

import static com.github.throyer.example.modules.infra.http.Responses.validateAndUpdateModel;

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

import com.github.throyer.example.modules.recoveries.dtos.RecoveryRequest;
import com.github.throyer.example.modules.ssr.dtos.Codes;
import com.github.throyer.example.modules.ssr.dtos.UpdatePasswordWithRecoveryCodeByApp;
import com.github.throyer.example.modules.ssr.services.AppRecoveryService;
import com.github.throyer.example.modules.ssr.toasts.Toasts;
import com.github.throyer.example.modules.ssr.toasts.Type;

@Controller
@RequestMapping("/app/recovery")
public class RecoveryController {

  @Autowired
  private AppRecoveryService service;

  @GetMapping
  public String index(Model model) {
    model.addAttribute("recovery", new RecoveryRequest());
    return "app/recovery/index";
  }

  @PostMapping
  public String index(
      @Valid RecoveryRequest recovery,
      BindingResult result,
      Model model) {

    if (validateAndUpdateModel(model, recovery, "recovery", result)) {
      return "app/recovery/index";
    }

    var email = recovery.getEmail();

    service.recovery(email);

    model.addAttribute("codes", new Codes(email));

    return "app/recovery/confirm";
  }

  @PostMapping("/confirm")
  public String confirm(
      @Valid Codes codes,
      BindingResult result,
      RedirectAttributes redirect,
      Model model) {

    if (validateAndUpdateModel(model, codes, "recovery", result)) {
      return "app/recovery/confirm";
    }

    try {
      service.confirm(codes.getEmail(), codes.code());
    } catch (ResponseStatusException exception) {

      Toasts.add(model, "Código expirado ou invalido.", Type.DANGER);
      model.addAttribute("confirm", codes);
      return "app/recovery/confirm";
    }

    model.addAttribute("update", new UpdatePasswordWithRecoveryCodeByApp(codes));

    return "app/recovery/update";
  }

  @PostMapping("/update")
  public String update(
      @Valid UpdatePasswordWithRecoveryCodeByApp update,
      BindingResult result,
      RedirectAttributes redirect,
      Model model) {
    update.validate(result);

    if (validateAndUpdateModel(model, update, "update", result)) {
      return "app/recovery/update";
    }

    try {
      service.update(update.getEmail(), update.code(), update.getPassword());
    } catch (ResponseStatusException exception) {
      Toasts.add(model, "Código expirado ou invalido.", Type.DANGER);
      model.addAttribute("update", update);
      return "app/recovery/update";
    }

    Toasts.add(redirect, "Sua senha foi atualizada com sucesso.", Type.SUCCESS);
    return "redirect:/app/login";
  }
}
