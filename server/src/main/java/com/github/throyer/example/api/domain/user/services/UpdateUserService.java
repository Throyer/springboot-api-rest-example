package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import com.github.throyer.example.api.domain.user.dtos.UpdateUserData;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.shared.rest.Responses.notFound;
import static com.github.throyer.example.api.shared.rest.Responses.unauthorized;

@Service
@AllArgsConstructor
public class UpdateUserService {
  private final UserRepository repository;

  public User update(Long id, UpdateUserData data) {
    Authorized
      .current()
      .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
      .orElseThrow(() -> unauthorized("Not authorized."));

    var actual = repository
      .findByIdFetchRoles(id)
      .orElseThrow(() -> notFound("User not found"));
        
    actual.setName(data.getName());

    repository.save(actual);

    return actual;
  }
}
