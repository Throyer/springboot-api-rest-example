package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.form.UpdateUserProps;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.common.springboot.constants.MESSAGES.NOT_AUTHORIZED_TO_MODIFY;
import static com.github.throyer.common.springboot.domain.mail.validation.EmailValidations.validateEmailUniquenessOnModify;
import static com.github.throyer.common.springboot.domain.session.service.SessionService.authorized;
import static com.github.throyer.common.springboot.utils.Messages.message;
import static com.github.throyer.common.springboot.utils.Responses.notFound;
import static com.github.throyer.common.springboot.utils.Responses.unauthorized;

@Service
public class UpdateUserService {
    private final UserRepository repository;

    @Autowired
    public UpdateUserService(UserRepository repository) {
        this.repository = repository;
    }

    public User update(Long id, UpdateUserProps body) {
        authorized()
            .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
                .orElseThrow(() -> unauthorized(message(NOT_AUTHORIZED_TO_MODIFY, "'user'")));

        var actual = repository
            .findByIdFetchRoles(id)
                .orElseThrow(() -> notFound("User not found"));
        
        validateEmailUniquenessOnModify(body, actual);

        actual.merge(body);

        repository.save(actual);

        return actual;
    }
}
