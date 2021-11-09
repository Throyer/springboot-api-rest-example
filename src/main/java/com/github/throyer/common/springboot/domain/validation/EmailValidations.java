package com.github.throyer.common.springboot.domain.validation;

import java.util.List;

import com.github.throyer.common.springboot.domain.models.shared.HasEmail;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@Component
public class EmailValidations {

    private static UserRepository repository;

    private static String FIELD = "email";
    private static String MESSAGE = "Este email já foi utilizado por outro usuário. Por favor utilize um email diferente.";

    private static final List<SimpleError> EMAIL_ERROR = List.of(new SimpleError(FIELD, MESSAGE));

    @Autowired
    public EmailValidations(UserRepository repository) {
        EmailValidations.repository = repository;
    }

    public static void validateEmailUniqueness(HasEmail entity) {
        if (repository.existsByEmail(entity.getEmail())) {
            throw new EmailNotUniqueException(EMAIL_ERROR);
        }
    }

    public static void validateEmailUniqueness(HasEmail entity, BindingResult result) {
        if (repository.existsByEmail(entity.getEmail())) {
            result.addError(new ObjectError(FIELD, MESSAGE));
        }
    }

    public static void validateEmailUniquenessOnModify(HasEmail newEntity, HasEmail actualEntity) {

        var newEmail = newEntity.getEmail();
        var actualEmail = actualEntity.getEmail();

        var changedEmail = !actualEmail.equals(newEmail);

        var emailAlreadyUsed = repository.existsByEmail(newEmail);

        if (changedEmail && emailAlreadyUsed) {
            throw new EmailNotUniqueException(EMAIL_ERROR);
        }
    }

    public static void validateEmailUniquenessOnModify(
        HasEmail newEntity,
        HasEmail actualEntity,
        BindingResult result
    ) {

        var newEmail = newEntity.getEmail();
        var actualEmail = actualEntity.getEmail();

        var changedEmail = !actualEmail.equals(newEmail);

        var emailAlreadyUsed = repository.existsByEmail(newEmail);

        if (changedEmail && emailAlreadyUsed) {
            result.addError(new ObjectError(FIELD, MESSAGE));
        }
    }
}
