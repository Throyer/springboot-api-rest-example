package com.github.throyer.example.modules.users.service;

import static com.github.throyer.example.modules.infra.constants.MessagesConstants.NOT_AUTHORIZED_TO_READ;
import static com.github.throyer.example.modules.infra.http.Responses.notFound;
import static com.github.throyer.example.modules.infra.http.Responses.unauthorized;
import static com.github.throyer.example.modules.shared.utils.InternationalizationUtils.message;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.example.modules.authentication.models.Authorized;
import com.github.throyer.example.modules.pagination.Page;
import com.github.throyer.example.modules.pagination.utils.Pagination;
import com.github.throyer.example.modules.users.entities.User;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class FindUserService {
  private final UserRepository repository;

  @Autowired
  public FindUserService(UserRepository repository) {
    this.repository = repository;
  }

  public User find(Long id) {
    Authorized.current()
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
