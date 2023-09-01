package com.github.throyer.example.api.domain.user.controllers;

import com.github.throyer.example.api.domain.user.services.FindAllUsersService;
import com.github.throyer.example.api.shared.pagination.Page;
import com.github.throyer.example.api.utils.ID;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.throyer.example.api.fixtures.UserFixture.users;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@Tag("Unit")
@ExtendWith(MockitoExtension.class)
public class ListUsersControllerTest {
  @InjectMocks
  private ListUsersController controller;

  @Mock
  private FindAllUsersService service;

  @Test
  void must_find_users() {
    try (MockedStatic<ID> utilities = mockStatic(ID.class)) {
      utilities.when(() -> ID.encode(anyLong()))
      .thenReturn("fake_id_encoded");
      var total = 1386;
      var size = 8;
      var page = 6;
      var content = users(size);

      when(service.find(anyInt(), anyInt()))
        .thenReturn(Page.of(content, page, size, 1386L));

      var result = assertDoesNotThrow(() -> controller.index(page, size));

      assertEquals(OK, result.getStatusCode());
      assertEquals(size, requireNonNull(result.getBody()).getSize());
      assertEquals(total, requireNonNull(result.getBody()).getTotalElements());
      assertEquals(size, requireNonNull(result.getBody()).getContent().size());
    }
  }
}
