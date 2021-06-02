package com.github.throyer.common.springboot.api.domain.services.user;

import static com.github.throyer.common.springboot.api.domain.services.security.SecurityService.authorized;
import static com.github.throyer.common.springboot.api.utils.Responses.notFound;
import static com.github.throyer.common.springboot.api.utils.Responses.ok;
import static com.github.throyer.common.springboot.api.utils.Responses.unauthorized;

import com.github.throyer.common.springboot.api.domain.models.entity.User;
import com.github.throyer.common.springboot.api.domain.models.pagination.Page;
import com.github.throyer.common.springboot.api.domain.models.pagination.Pagination;
import com.github.throyer.common.springboot.api.domain.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FindUserService {

    @Autowired
    UserRepository repository;

    public ResponseEntity<Page<User>> find(Pagination pagination) {
        return ok(Page.of(repository.findAll(pagination.build())));
    }

    public ResponseEntity<Page<User>> find(Pagination pagination, Sort sort) {
        return ok(Page.of(repository.findDistinctBy(pagination.build(sort, User.class))));
    }

    public ResponseEntity<User> find(Long id) {
        return authorized()
            .filter(authorized -> authorized.cantRead(id))
            .map((authorized) -> 
                repository
                    .findById(id)
                        .map(user -> ok(user))
                .orElseGet(() -> notFound()))
            .orElse(unauthorized());
    }
}
