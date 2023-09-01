package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.role.persistence.repositories.RoleRepository;
import com.github.throyer.example.api.domain.user.dtos.CreateUserData;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.github.throyer.example.api.fixtures.RoleFixture.roles;
import static com.github.throyer.example.api.fixtures.UserFixture.*;
import static com.github.throyer.example.api.utils.Random.element;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;

@Tag("Unit")
@ExtendWith(MockitoExtension.class)
public class CreateUserServiceTest {
  @InjectMocks
  private CreateUserService service;

  @Mock
  private UserRepository userRepository;

  @Mock
  private RoleRepository roleRepository;

  @Test
  void must_create_user_successfully() {
    var role = element(roles());
    var name = name();
    var email = email();
    var password = password();

    var no = false;

    var data = new CreateUserData(
      name,
      email,
      password
    );

    when(userRepository.existsByEmail(anyString())).thenReturn(no);
    when(roleRepository.findOptionalByShortName(anyString())).thenReturn(Optional.of(role));
    doNothing().when(userRepository).save(any(User.class));

    assertDoesNotThrow(() -> service.create(data));
  }

  @Test
  void cannot_create_user_when_email_has_already_used() {
    var name = name();
    var email = email();
    var password = password();

    var yes = true;

    var data = new CreateUserData(
      name,
      email,
      password
    );

    when(userRepository.existsByEmail(anyString())).thenReturn(yes);

    var exception = assertThrowsExactly(ResponseStatusException.class, () -> service.create(data));

    assertTrue(exception.getStatusCode().isSameCodeAs(CONFLICT));
  }
}
