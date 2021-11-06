package com.github.throyer.common.springboot.domain.services.user;

import static com.github.throyer.common.springboot.utils.Responses.noContent;
import static com.github.throyer.common.springboot.utils.Responses.notFound;

import com.github.throyer.common.springboot.domain.models.entity.User;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RemoveUserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<User> remove(Long id) {
        return repository.findOptionalByIdAndDeletedAtIsNull(id)
            .map(user -> noContent(user, repository))
                .orElseGet(() -> notFound());
    }
}
