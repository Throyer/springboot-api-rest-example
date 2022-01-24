package com.github.throyer.common.springboot.domain.mail.validation;

import com.github.throyer.common.springboot.domain.mail.exceptions.EmailNotUniqueException;
import com.github.throyer.common.springboot.domain.mail.model.Addressable;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import static com.github.throyer.common.springboot.utils.Constants.MAIL.*;

@Component
public class EmailValidations {

    private static UserRepository repository;

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
            result.addError(new ObjectError(EMAIL_FIELD, EMAIL_ALREADY_USED_MESSAGE));
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
            result.addError(new ObjectError(EMAIL_FIELD, EMAIL_ALREADY_USED_MESSAGE));
        }
    }
}
