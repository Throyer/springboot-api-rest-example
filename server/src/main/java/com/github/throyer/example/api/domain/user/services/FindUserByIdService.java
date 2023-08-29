package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.shared.rest.Responses.notFound;
import static com.github.throyer.example.api.shared.rest.Responses.unauthorized;

@Service
@AllArgsConstructor
public class FindUserByIdService {
  private final UserRepository repository;
  
  public User find(Long id) {
    Authorized
      .current()
      .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
      .orElseThrow(() -> unauthorized("Not authorized."));

    return repository
      .findByIdFetchRoles(id)
      .orElseThrow(() -> notFound("Not found"));
  }
}
