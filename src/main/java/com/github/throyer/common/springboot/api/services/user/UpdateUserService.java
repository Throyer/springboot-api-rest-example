package com.github.throyer.common.springboot.api.services.user;

import static com.github.throyer.common.springboot.api.services.common.validation.EmailValidations.validateEmailUniquenessOnModify;
import static com.github.throyer.common.springboot.api.services.security.SecurityService.authorized;
import static com.github.throyer.common.springboot.api.utils.Responses.notFound;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;
import static com.github.throyer.common.springboot.api.utils.Responses.unauthorized;

import com.github.throyer.common.springboot.api.models.entity.User;
import com.github.throyer.common.springboot.api.repositories.UserRepository;
import com.github.throyer.common.springboot.api.services.user.dto.UpdateUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<User> update(Long id, UpdateUser body) {

        authorized()
            .filter(authorized -> authorized.cantModify(id))
                .orElseThrow(() -> unauthorized("Permissão invalida atualização de recurso"));

        var actual = repository
            .findById(id)
                .orElseThrow(() -> notFound("Usuário não encontrado"));
        
        validateEmailUniquenessOnModify(body, actual);

        actual.merge(body);

        return ok(repository.save(actual));
    }
}
