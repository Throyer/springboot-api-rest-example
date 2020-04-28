package com.github.throyer.common.springboot.api.models.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.throyer.common.springboot.api.models.validation.constraints.EmailExists;
import com.github.throyer.common.springboot.api.repositories.UsuarioRepository;

public class EmailValidator implements ConstraintValidator<EmailExists, String> {

    private UsuarioRepository repository;

    public EmailValidator(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !repository.existsByEmail(email);
    }

}
