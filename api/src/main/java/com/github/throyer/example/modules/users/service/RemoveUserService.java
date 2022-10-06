package com.github.throyer.example.modules.users.service;

import static com.github.throyer.example.modules.infra.constants.MessagesConstants.NOT_AUTHORIZED_TO_MODIFY;
import static com.github.throyer.example.modules.shared.utils.InternationalizationUtils.message;
import static com.github.throyer.example.modules.infra.http.Responses.notFound;
import static com.github.throyer.example.modules.infra.http.Responses.unauthorized;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.throyer.example.modules.authentication.models.Authorized;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@Service
public class RemoveUserService {
  @Autowired
  UserRepository repository;

  public void remove(Long id) {
    Authorized
      .current()
        .filter(authorized -> authorized.itsMeOrSessionIsADM(id))
          .orElseThrow(() -> unauthorized(message(NOT_AUTHORIZED_TO_MODIFY, "'user'")));
      
    var user = repository
        .findById(id)
            .orElseThrow(() -> notFound("User not found")); 
    
    repository.delete(user);
  }
}
