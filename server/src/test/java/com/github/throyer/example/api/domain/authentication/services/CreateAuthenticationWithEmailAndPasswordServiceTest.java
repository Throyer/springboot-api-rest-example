package com.github.throyer.example.api.domain.authentication.services;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithEmailAndPassword;
import com.github.throyer.example.api.domain.authentication.persistence.models.RefreshToken;
import com.github.throyer.example.api.domain.authentication.persistence.repositories.RefreshTokenRepository;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import com.github.throyer.example.api.shared.jwt.JWT;
import com.github.throyer.example.api.utils.ID;
import com.github.throyer.example.api.utils.Passwords;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.github.throyer.example.api.fixtures.UserFixture.user;
import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Tag("Unit")
@ExtendWith(MockitoExtension.class)
class CreateAuthenticationWithEmailAndPasswordServiceTest {
  @InjectMocks
  private CreateAuthenticationWithEmailAndPasswordService service;
  
  @Mock
  private RefreshTokenRepository refreshTokenRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private SecurityProperties properties;

  @Mock
  private JWT jwt;

  @Mock
  private Internationalization i18n;

  @Test
  void must_successfully_create_authentication() {
    try (
      MockedStatic<Passwords> passwords = mockStatic(Passwords.class);
      MockedStatic<ID> ids = mockStatic(ID.class)
    ) {
      
      passwords.when(() -> Passwords.matches(anyString(), anyString()))
        .thenReturn(true);

      ids.when(() -> ID.encode(anyLong()))
        .thenReturn("fake-encoded-id");
      
      when(jwt.encode(anyString(), anyList(), any(LocalDateTime.class), anyString()))
        .thenReturn("fake-json-web-token");
      
      when(properties.getTokenSecret())
        .thenReturn("very-strong-secret-code");

      when(properties.getTokenExpirationInHours())
        .thenReturn(1);
      
      when(properties.getRefreshTokenExpirationInDays())
        .thenReturn(1);
      
      doNothing()
        .when(refreshTokenRepository)
          .disableOldRefreshTokensFromUser(anyLong());
      
      when(refreshTokenRepository.save(any(RefreshToken.class)))
        .thenReturn(null);
      
      var user = user();

      when(userRepository.findOptionalByEmail(anyString()))
        .thenReturn(Optional.of(user));

      var body = new CreateAuthenticationWithEmailAndPassword(
        user.getEmail(),
        user.getPassword()
      );

      assertDoesNotThrow(() -> service.create(body));
      
      verify(properties, times(1)).getTokenSecret();
      verify(properties, times(1)).getTokenExpirationInHours();
      verify(properties, times(1)).getRefreshTokenExpirationInDays();
      
      verify(jwt, times(1)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
    }    
  }
  
  @Test
  void should_fail_when_not_finding_user() {
    var body = new CreateAuthenticationWithEmailAndPassword(
      "clebinho_da_sila@email.com",
      "definitivamente_essa_senha_é_bem_segura"
    );

    when(userRepository.findOptionalByEmail(anyString()))
      .thenReturn(empty());

    when(i18n.message(anyString()))
      .thenReturn("Invalid password or username.");

    var exception = assertThrowsExactly(ResponseStatusException.class, () -> service.create(body));
    
    assertEquals("Invalid password or username.", exception.getReason());
    assertTrue(exception.getStatusCode().isSameCodeAs(FORBIDDEN));

    verify(properties, times(0)).getTokenSecret();
    verify(properties, times(0)).getTokenExpirationInHours();
    verify(properties, times(0)).getRefreshTokenExpirationInDays();

    verify(jwt, times(0)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
  }
  
  @Test
  void should_fail_when_password_is_incorrect() {
    try (MockedStatic<Passwords> passwords = mockStatic(Passwords.class)) {
      
      passwords.when(() -> Passwords.matches(anyString(), anyString()))
        .thenReturn(false);
      
      var body = new CreateAuthenticationWithEmailAndPassword(
        "clebinho_da_sila@email.com",
        "definitivamente_essa_senha_é_bem_segura"
      );

      var user = user();

      when(userRepository.findOptionalByEmail(anyString()))
        .thenReturn(Optional.of(user));

      when(i18n.message(anyString()))
        .thenReturn("Invalid password or username.");

      var exception = assertThrowsExactly(ResponseStatusException.class, () -> service.create(body));

      assertEquals("Invalid password or username.", exception.getReason());
      assertTrue(exception.getStatusCode().isSameCodeAs(FORBIDDEN));

      verify(properties, times(0)).getTokenSecret();
      verify(properties, times(0)).getTokenExpirationInHours();
      verify(properties, times(0)).getRefreshTokenExpirationInDays();

      verify(jwt, times(0)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
    }    
  }
  
  @Test
  void should_fail_when_email_is_not_confirmed() {
    try (MockedStatic<Passwords> passwords = mockStatic(Passwords.class)) {

      passwords.when(() -> Passwords.matches(anyString(), anyString()))
        .thenReturn(true);

      var body = new CreateAuthenticationWithEmailAndPassword(
        "clebinho_da_sila@email.com",
        "definitivamente_essa_senha_é_bem_segura"
      );

      var user = user(false);

      when(userRepository.findOptionalByEmail(anyString()))
        .thenReturn(Optional.of(user));

      when(i18n.message(anyString()))
        .thenReturn("Email was not confirmed.");

      var exception = assertThrowsExactly(ResponseStatusException.class, () -> service.create(body));

      assertEquals("Email was not confirmed.", exception.getReason());
      assertTrue(exception.getStatusCode().isSameCodeAs(FORBIDDEN));

      verify(properties, times(0)).getTokenSecret();
      verify(properties, times(0)).getTokenExpirationInHours();
      verify(properties, times(0)).getRefreshTokenExpirationInDays();

      verify(jwt, times(0)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
    }
  }
}
