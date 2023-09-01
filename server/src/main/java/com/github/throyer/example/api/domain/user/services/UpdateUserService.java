package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import com.github.throyer.example.api.domain.user.dtos.UpdateUserData;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.github.throyer.example.api.shared.rest.Responses.notFound;
import static com.github.throyer.example.api.shared.rest.Responses.unauthorized;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class UpdateUserService {
  private final UserRepository repository;

  public User update(Long id, UpdateUserData data) {
    var session = Authorized.current()
      .orElseThrow(() -> unauthorized("Not authorized."));

    if (!session.itsMeOrSessionIsADM(id)) {
      throw unauthorized("Not authorized.");
    }

    var actual = repository
      .findByIdFetchRoles(id)
        .orElseThrow(() -> notFound("User not found"));
        
    actual.setName(data.getName());

    actual.setUpdatedBy(new User(session.getId()));
    actual.setUpdatedAt(now());
    repository.save(actual);

    return actual;
  }
}
