package com.github.throyer.example.api.domain.user.services;

import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.shared.pagination.Page;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import static com.github.throyer.example.api.fixtures.UserFixture.users;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Tag("Unit")
@ExtendWith(MockitoExtension.class)
public class FindAllUsersServiceTest {
  @InjectMocks
  private FindAllUsersService service;

  @Mock
  private UserRepository userRepository;

  @Test
  void must_find_users() {
    var total = 1386;
    var size = 8;
    var page = 6;
    var content = users(size);

    when(userRepository.findAllFetchRoles(any(Pageable.class)))
      .thenReturn(Page.of(content, page, size, 1386L));

    var result = assertDoesNotThrow(() -> service.find(page, size));

    assertEquals(page, result.getPage());
    assertEquals(size, result.getSize());
    assertEquals(total, result.getTotalElements());
    assertEquals(size, result.getContent().size());
  }
}
