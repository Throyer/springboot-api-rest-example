package com.github.throyer.example.modules.users.controllers;

import static com.github.throyer.example.modules.shared.utils.HashIdsUtils.decode;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.throyer.example.modules.pagination.utils.Pagination;
import com.github.throyer.example.modules.toasts.Toasts;
import com.github.throyer.example.modules.toasts.Type;
import com.github.throyer.example.modules.users.dtos.CreateOrUpdateUserByAppForm;
import com.github.throyer.example.modules.users.repositories.UserRepository;
import com.github.throyer.example.modules.users.service.RemoveUserService;

@Controller
@PreAuthorize("hasAnyAuthority('ADM')")
@RequestMapping("/app/users")
public class UserController {    
  @Autowired
  private UserRepository repository;
  
  @Autowired
  private RemoveUserService removeService;
  
  @GetMapping
  public String index(
    Model model,
    Optional<Integer> page,
    Optional<Integer> size
  ) {
    var pageable = Pagination.of(page, size);
    var content = repository.findAll(pageable);
    
    model.addAttribute("page", content);
    
    return "app/users/index";
  }

  @GetMapping("/form")
  public String createForm(Model model) {
    model.addAttribute("create", true);
    return "app/users/form";
  }

  @GetMapping("/form/{user_id}")
  public String editForm(@PathVariable("user_id") String id, Model model) {
    model.addAttribute("create", false);
    return "app/users/form";
  }

  @PostMapping(produces = "text/html")
  public String create(
    @Valid CreateOrUpdateUserByAppForm props,
    BindingResult validations,
    RedirectAttributes redirect,
    Model model
  ) {
    if (validations.hasErrors()) {
        return "app/users/index";
    }

    return "redirect:/app/users/form";
  }
  
  @PostMapping("/delete/{user_id}")
  public String delete(@PathVariable("user_id") String id, RedirectAttributes redirect) {
    
    removeService.remove(decode(id));
    
    Toasts.add(redirect, "Usuário deletado com sucesso.", Type.SUCCESS);
    
    return "redirect:/app/users";
  }
}
