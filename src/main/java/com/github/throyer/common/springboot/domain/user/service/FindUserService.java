package com.github.throyer.common.springboot.domain.user.service;

import com.github.throyer.common.springboot.domain.pagination.model.Page;
import com.github.throyer.common.springboot.domain.pagination.service.Pagination;
import com.github.throyer.common.springboot.domain.user.entity.User;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.github.throyer.common.springboot.constants.MESSAGES.NOT_AUTHORIZED_TO_READ;
import static com.github.throyer.common.springboot.domain.session.service.SessionService.authorized;
import static com.github.throyer.common.springboot.utils.Messages.message;
import static com.github.throyer.common.springboot.utils.Responses.notFound;
import static com.github.throyer.common.springboot.utils.Responses.unauthorized;

@Service
public class FindUserService {
    private final UserRepository repository;

    @Autowired
    public FindUserService(UserRepository repository) {
        this.repository = repository;
    }

    public User find(Long id) {
        authorized()
            .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
                .orElseThrow(() -> unauthorized(message(NOT_AUTHORIZED_TO_READ, "'user'")));

        return repository
            .findById(id)
                .orElseThrow(() -> notFound("Not found"));
    }

    public Page<User> find(Optional<Integer> page, Optional<Integer> size) {
        var pageable = Pagination.of(page, size);
        return repository.findAll(pageable);
    }
}
