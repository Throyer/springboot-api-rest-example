package com.github.throyer.example.modules.users.service;

import static com.github.throyer.example.modules.infra.constants.MessagesConstants.NOT_AUTHORIZED_TO_MODIFY;
import static com.github.throyer.example.modules.infra.http.Responses.notFound;
import static com.github.throyer.example.modules.infra.http.Responses.unauthorized;
import static com.github.throyer.example.modules.mail.validations.EmailValidations.validateEmailUniquenessOnModify;
import static com.github.throyer.example.modules.shared.utils.InternationalizationUtils.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.example.modules.authentication.models.Authorized;
import com.github.throyer.example.modules.users.dtos.UpdateUserProps;
import com.github.throyer.example.modules.users.entities.User;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class UpdateUserService {
    private final UserRepository repository;

    @Autowired
    public UpdateUserService(UserRepository repository) {
        this.repository = repository;
    }

    public User update(Long id, UpdateUserProps body) {
      Authorized
        .current()
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
