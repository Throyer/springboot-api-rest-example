package com.github.throyer.common.springboot.domain.services.user;

import static com.github.throyer.common.springboot.domain.services.security.SecurityService.authorized;
import static com.github.throyer.common.springboot.domain.validation.EmailValidations.validateEmailUniquenessOnModify;
import static com.github.throyer.common.springboot.utils.Responses.notFound;
import static com.github.throyer.common.springboot.utils.Responses.ok;
import static com.github.throyer.common.springboot.utils.Responses.unauthorized;

import com.github.throyer.common.springboot.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.domain.services.user.dto.UpdateUser;
import com.github.throyer.common.springboot.domain.services.user.dto.UserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UpdateUserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<UserDetails> update(Long id, UpdateUser body) {

        authorized()
            .filter(authorized -> authorized.cantModify(id))
                .orElseThrow(() -> unauthorized("Permissão invalida atualização de recurso"));

        var actual = repository
            .findOptionalByIdAndDeletedAtIsNullFetchRoles(id)
                .orElseThrow(() -> notFound("Usuário não encontrado"));
        
        validateEmailUniquenessOnModify(body, actual);

        actual.merge(body);

        return ok(new UserDetails(repository.save(actual)));
    }
}
