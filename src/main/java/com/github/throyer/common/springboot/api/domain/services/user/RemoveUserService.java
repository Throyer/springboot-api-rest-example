package com.github.throyer.common.springboot.api.domain.services.user;

import static com.github.throyer.common.springboot.api.utils.Responses.noContent;
import static com.github.throyer.common.springboot.api.utils.Responses.notFound;

import com.github.throyer.common.springboot.api.domain.models.entity.User;
import com.github.throyer.common.springboot.api.domain.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RemoveUserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<User> remove(Long id) {
        return repository.findById(id)
            .map(user -> noContent(user, repository))
                .orElseGet(() -> notFound());
    }
}
