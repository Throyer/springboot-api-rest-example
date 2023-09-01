package com.github.throyer.example.api.domain.user.controllers;

import com.github.throyer.example.api.domain.user.dtos.CreateUserData;
import com.github.throyer.example.api.domain.user.services.CreateUserService;
import com.github.throyer.example.api.utils.ID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import static com.github.throyer.example.api.fixtures.RoleFixture.roles;
import static com.github.throyer.example.api.fixtures.UserFixture.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;

@Tag("Unit")
@ExtendWith(MockitoExtension.class)
public class CreateUserControllerTest {
  @InjectMocks
  private CreateUserController controller;

  @Mock
  private CreateUserService service;
    
  @Test
  void must_create_user_successfully() {
    try (MockedStatic<ID> utilities = mockStatic(ID.class)) {
      utilities.when(() -> ID.encode(anyLong()))
      .thenReturn("fake_id_encoded");

      var name = name();
      var email = email();
      var password = password();

      var data = new CreateUserData(
        name,
        email,
        password
      );

      when(service.create(any(CreateUserData.class))).thenReturn(user(
        1L,
        name,
        email,
        password,
        roles()
      ));

      assertDoesNotThrow(() -> controller.create(data));
    }    
  }

  @Test
  void cannot_create_user_when_email_has_already_used() {    
    var data = new CreateUserData(
      name(),
      email(),
      password()
    );

    when(service.create(any(CreateUserData.class))).thenThrow(new ResponseStatusException(CONFLICT));

    var exception = assertThrowsExactly(ResponseStatusException.class, () -> controller.create(data));

    assertTrue(exception.getStatusCode().isSameCodeAs(CONFLICT));
  }
}
