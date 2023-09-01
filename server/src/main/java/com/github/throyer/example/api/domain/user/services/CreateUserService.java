package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.role.persistence.repositories.RoleRepository;
import com.github.throyer.example.api.domain.user.dtos.CreateUserData;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.github.throyer.example.api.infra.constants.SecurityConstants.Roles.USER;
import static com.github.throyer.example.api.shared.i18n.Internationalization.CREATE_USER_EMAIL_UNAVAILABLE_MESSAGE;
import static com.github.throyer.example.api.shared.i18n.Internationalization.CREATE_USER_FAIL_ON_QUERY_USER_ROLE;
import static com.github.throyer.example.api.shared.rest.Responses.InternalServerError;
import static com.github.throyer.example.api.shared.rest.Responses.conflict;

@Slf4j
@Service
@AllArgsConstructor
public class CreateUserService {
  private final Internationalization i18n;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  public User create(CreateUserData data) {
    if (userRepository.existsByEmail(data.getEmail())) {
      log.warn("could not create user, e-mail is unavailable.");
      throw conflict(i18n.message(CREATE_USER_EMAIL_UNAVAILABLE_MESSAGE));
    }

    var role = roleRepository.findOptionalByShortName(USER)
      .orElseThrow(() -> InternalServerError(i18n.message(CREATE_USER_FAIL_ON_QUERY_USER_ROLE, "USER")));

    var user = new User(data.getName(), data.getEmail(), data.getPassword(), role);

    userRepository.save(user);

    log.info("new user successfully created.");

    return user;
  }
}
