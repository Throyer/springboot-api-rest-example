package com.github.throyer.example.modules.ssr.services;

import static com.github.throyer.example.modules.infra.constants.ToastConstants.TOAST_SUCCESS_MESSAGE;
import static com.github.throyer.example.modules.infra.http.Responses.notFound;
import static com.github.throyer.example.modules.ssr.validation.AppEmailValidations.validateEmailUniqueness;
import static com.github.throyer.example.modules.shared.utils.InternationalizationUtils.message;
import static com.github.throyer.example.modules.ssr.toasts.Type.SUCCESS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.throyer.example.modules.roles.repositories.RoleRepository;
import com.github.throyer.example.modules.ssr.dtos.CreateUserByApp;
import com.github.throyer.example.modules.ssr.toasts.Toasts;
import com.github.throyer.example.modules.users.entities.User;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class AppUserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Autowired
  public AppUserService(
    UserRepository userRepository,
    RoleRepository roleRepository
  ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  public void create(
    CreateUserByApp props,
    BindingResult validations,
    RedirectAttributes redirect,
    Model model
  ) {
    validateEmailUniqueness(props, validations);

    if (validations.hasErrors()) {
      model.addAttribute("user", props);
      Toasts.add(model, validations);
      return;
    }
  
    Toasts.add(redirect, message(TOAST_SUCCESS_MESSAGE), SUCCESS);

    var roles = roleRepository.findOptionalByShortName("USER")
      .map(List::of)
        .orElseGet(List::of);

    var name = props.getName();
    var email = props.getEmail();
    var password = props.getPassword();

    userRepository.save(new User(name, email, password, roles));
  }

  public void remove(Long id) {      
    var user = userRepository
      .findById(id)
        .orElseThrow(() -> notFound("User not found")); 

    userRepository.delete(user);
  }
}
