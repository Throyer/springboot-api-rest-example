package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.github.throyer.example.api.shared.rest.Responses.notFound;
import static com.github.throyer.example.api.shared.rest.Responses.unauthorized;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class RemoveUserService {
  private final UserRepository repository;

  public void remove(Long id) {
    Authorized
      .current()
      .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
      .orElseThrow(() -> unauthorized("Not authorized."));

    var user = repository
      .findByIdFetchRoles(id)
      .orElseThrow(() -> notFound("User not found"));

    user.setDeletedAt(now());
    
    repository.save(user);
  }
}
