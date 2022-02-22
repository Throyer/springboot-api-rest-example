package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.user.form.UpdateUserProps;
import com.github.throyer.common.springboot.domain.user.model.UserDetails;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.throyer.common.springboot.domain.mail.validation.EmailValidations.validateEmailUniquenessOnModify;
import static com.github.throyer.common.springboot.domain.session.service.SessionService.authorized;
import static com.github.throyer.common.springboot.utils.Responses.notFound;
import static com.github.throyer.common.springboot.utils.Responses.unauthorized;

@Service
public class UpdateUserService {
    
    @Autowired
    UserRepository repository;

    public UserDetails update(Long id, UpdateUserProps body) {

        authorized()
            .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
                .orElseThrow(() -> unauthorized("Permission invalidates resource update"));

        var actual = repository
            .findById(id)
                .orElseThrow(() -> notFound("User not found"));
        
        validateEmailUniquenessOnModify(body, actual);

        actual.merge(body);

        return new UserDetails(repository.save(actual));
    }
}
