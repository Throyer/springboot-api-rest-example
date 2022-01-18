package com.github.throyer.common.springboot.domain.mail.exceptions;

import java.util.List;

import com.github.throyer.common.springboot.domain.user.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import com.github.throyer.common.springboot.domain.management.model.Addressable;
import com.github.throyer.common.springboot.errors.Error;

@Component
public class EmailValidations {

    private static UserRepository repository;

    private static String FIELD = "email";
    private static String MESSAGE = "This email has already been used by another user. Please use a different email.";

    private static final List<Error> EMAIL_ERROR = List.of(new Error(FIELD, MESSAGE));

    @Autowired
    public EmailValidations(UserRepository repository) {
        EmailValidations.repository = repository;
    }

    public static void validateEmailUniqueness(Addressable entity) {
        if (repository.existsByEmail(entity.getEmail())) {
            throw new EmailNotUniqueException(EMAIL_ERROR);
        }
    }

    public static void validateEmailUniqueness(Addressable entity, BindingResult result) {
        if (repository.existsByEmail(entity.getEmail())) {
            result.addError(new ObjectError(FIELD, MESSAGE));
        }
    }

    public static void validateEmailUniquenessOnModify(Addressable newEntity, Addressable actualEntity) {

        var newEmail = newEntity.getEmail();
        var actualEmail = actualEntity.getEmail();

        var changedEmail = !actualEmail.equals(newEmail);

        var emailAlreadyUsed = repository.existsByEmail(newEmail);

        if (changedEmail && emailAlreadyUsed) {
            throw new EmailNotUniqueException(EMAIL_ERROR);
        }
    }

    public static void validateEmailUniquenessOnModify(
        Addressable newEntity,
        Addressable actualEntity,
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
