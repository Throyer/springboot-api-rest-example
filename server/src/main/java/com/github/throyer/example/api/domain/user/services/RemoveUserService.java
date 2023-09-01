package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
    var session = Authorized.current()
      .orElseThrow(() -> unauthorized("Not authorized."));
    
    if (!session.itsMeOrSessionIsADM(id)) {
      throw unauthorized("Not authorized.");  
    }
    
    var user = repository
      .findByIdFetchRoles(id)
        .orElseThrow(() -> notFound("User not found"));

    user.setDeletedEmail(user.getEmail());
    user.setDeletedAt(now());
    user.setDeletedBy(new User(session.getId()));
    user.setEmail(null);
    
    repository.save(user);
  }
}
