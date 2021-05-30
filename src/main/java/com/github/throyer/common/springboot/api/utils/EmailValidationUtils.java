package com.github.throyer.common.springboot.api.utils;

import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.User;
import com.github.throyer.common.springboot.api.models.validation.EmailNotUniqueException;
import com.github.throyer.common.springboot.api.models.validation.SimpleError;
import com.github.throyer.common.springboot.api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationUtils {

    private static UserRepository repository;

    private static String FIELD = "email";
    private static String MESSAGE = "Este email já foi utilizado por outro usuário. Por favor utilize um email diferente.";

    private static final List<SimpleError> EMAIL_ERROR = List.of(new SimpleError(FIELD, MESSAGE));

    @Autowired
    public EmailValidationUtils(UserRepository repository) {
        EmailValidationUtils.repository = repository;
    }

    public static void validateEmailUniqueness(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new EmailNotUniqueException(EMAIL_ERROR);
        }
    }

    public static void validateEmailUniquenessEdition(User novo, User atual) {

        var newEmail = novo.getEmail();
        var actualEmail = atual.getEmail();

        var changedEmail = !actualEmail.equals(newEmail);

        var emailAlreadyUsed = repository.existsByEmail(newEmail);

        if (changedEmail && emailAlreadyUsed) {
            throw new EmailNotUniqueException(EMAIL_ERROR);
        }
    }
}