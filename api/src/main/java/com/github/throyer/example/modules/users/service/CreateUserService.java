package com.github.throyer.example.modules.users.service;

import static com.github.throyer.example.modules.mail.validations.EmailValidations.validateEmailUniqueness;
import static com.github.throyer.example.modules.infra.constants.ToastConstants.TOAST_SUCCESS_MESSAGE;
import static com.github.throyer.example.modules.shared.utils.InternationalizationUtils.message;
import static com.github.throyer.example.modules.toasts.Type.SUCCESS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.github.throyer.example.modules.roles.repositories.RoleRepository;
import com.github.throyer.example.modules.toasts.Toasts;
import com.github.throyer.example.modules.users.dtos.CreateUserProps;
import com.github.throyer.example.modules.users.entities.User;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class CreateUserService {
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;

  @Autowired
  public CreateUserService(
      UserRepository userRepository,
      RoleRepository roleRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
  }

  public User create(CreateUserProps props) {
    validateEmailUniqueness(props);
    return save(props);
  }

  public void create(
      CreateUserProps props,
      BindingResult validations,
      RedirectAttributes redirect,
      Model model) {
    validateEmailUniqueness(props, validations);

    if (validations.hasErrors()) {
      model.addAttribute("user", props);
      Toasts.add(model, validations);
      return;
    }

    Toasts.add(redirect, message(TOAST_SUCCESS_MESSAGE), SUCCESS);
    save(props);
  }

  private User save(CreateUserProps props) {
    var roles = roleRepository.findOptionalByShortName("USER")
        .map(List::of)
        .orElseGet(List::of);
    return userRepository.save(new User(props, roles));
  }
}
