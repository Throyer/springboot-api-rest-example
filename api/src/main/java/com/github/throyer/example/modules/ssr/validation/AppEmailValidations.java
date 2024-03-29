package com.github.throyer.example.modules.ssr.validation;

import static com.github.throyer.example.modules.infra.constants.MessagesConstants.EMAIL_ALREADY_USED_MESSAGE;
import static com.github.throyer.example.modules.shared.utils.InternationalizationUtils.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.github.throyer.example.modules.mail.models.Addressable;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Component
public class AppEmailValidations {
  private static UserRepository repository;

  @Autowired
  public AppEmailValidations(UserRepository repository) {
    AppEmailValidations.repository = repository;
  }

  public static void validateEmailUniqueness(Addressable entity, BindingResult validations) {
    if (repository.existsByEmail(entity.getEmail())) {
      validations.addError(new ObjectError("email", message(EMAIL_ALREADY_USED_MESSAGE)));
    }
  }

  public static void validateEmailUniquenessOnModify(
      Addressable newEntity,
      Addressable actualEntity,
      BindingResult validations) {
    var newEmail = newEntity.getEmail();
    var actualEmail = actualEntity.getEmail();

    var changedEmail = !actualEmail.equals(newEmail);

    var emailAlreadyUsed = repository.existsByEmail(newEmail);

    if (changedEmail && emailAlreadyUsed) {
      validations.addError(new ObjectError("email", message(EMAIL_ALREADY_USED_MESSAGE)));
    }
  }
}
