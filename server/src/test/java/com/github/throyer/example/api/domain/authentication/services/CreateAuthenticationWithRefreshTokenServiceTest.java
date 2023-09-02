package com.github.throyer.example.api.domain.authentication.services;

import com.github.throyer.example.api.domain.authentication.dtos.CreateAuthenticationWithRefreshToken;
import com.github.throyer.example.api.domain.authentication.persistence.models.RefreshToken;
import com.github.throyer.example.api.domain.authentication.persistence.repositories.RefreshTokenRepository;
import com.github.throyer.example.api.domain.user.persistence.models.User;
import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.shared.i18n.Internationalization;
import com.github.throyer.example.api.shared.jwt.JWT;
import com.github.throyer.example.api.utils.ID;
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
class CreateAuthenticationWithRefreshTokenServiceTest {
  @InjectMocks
  private CreateAuthenticationWithRefreshTokenService service;
  
  @Mock
  private RefreshTokenRepository refreshTokenRepository;

  @Mock
  private SecurityProperties properties;

  @Mock
  private JWT jwt;
  
  @Mock
  private Internationalization i18n;
  
  @Test
  void must_successfully_create_authentication() {
    try (MockedStatic<ID> identityEncoderMock = mockStatic(ID.class)) {

      identityEncoderMock.when(() -> ID.encode(anyLong()))
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

      var refresh = new RefreshToken(user, 1);
      
      when(refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(anyString()))
        .thenReturn(Optional.of(refresh));

      var body = new CreateAuthenticationWithRefreshToken(refresh.getCode());

      assertDoesNotThrow(() -> service.create(body));

      verify(properties, times(1)).getTokenSecret();
      verify(properties, times(1)).getTokenExpirationInHours();
      verify(properties, times(1)).getRefreshTokenExpirationInDays();

      verify(jwt, times(1)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
    }
  }

  @Test
  void should_fail_when_not_finding_refresh_token() {
    var user = user();
    var refresh = new RefreshToken(user, 1);
    var body = new CreateAuthenticationWithRefreshToken(refresh.getCode());
    
    when(i18n.message(anyString()))
      .thenReturn("Refresh token expired or invalid.");
    
    when(refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(anyString()))
      .thenReturn(empty());

    var exception = assertThrowsExactly(ResponseStatusException.class, () -> service.create(body));

    assertEquals("Refresh token expired or invalid.", exception.getReason());
    assertTrue(exception.getStatusCode().isSameCodeAs(FORBIDDEN));

    verify(properties, times(0)).getTokenSecret();
    verify(properties, times(0)).getTokenExpirationInHours();
    verify(properties, times(0)).getRefreshTokenExpirationInDays();

    verify(jwt, times(0)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
  }

  @Test
  void should_fail_when_refresh_token_is_expired() {
    var code = "fake-refresh-token-code";

    var refresh = mock(RefreshToken.class);

    when(i18n.message(anyString()))
      .thenReturn("Refresh token expired or invalid.");

    when(refresh.isExpired()).thenReturn(true);

    var body = new CreateAuthenticationWithRefreshToken(code);

    when(refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(anyString()))
      .thenReturn(Optional.of(refresh));

    var exception = assertThrowsExactly(ResponseStatusException.class, () -> service.create(body));

    assertEquals("Refresh token expired or invalid.", exception.getReason());
    assertTrue(exception.getStatusCode().isSameCodeAs(FORBIDDEN));

    verify(properties, times(0)).getTokenSecret();
    verify(properties, times(0)).getTokenExpirationInHours();
    verify(properties, times(0)).getRefreshTokenExpirationInDays();

    verify(jwt, times(0)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
  }

  @Test
  void should_fail_when_email_is_not_confirmed() {
    var code = "fake-refresh-token-code";

    var refresh = mock(RefreshToken.class);
    var user = mock(User.class);

    when(user.emailHasConfirmed()).thenReturn(false);
    when(refresh.getUser()).thenReturn(user);
    when(refresh.isExpired()).thenReturn(false);

    var body = new CreateAuthenticationWithRefreshToken(code);

    when(i18n.message(anyString()))
      .thenReturn("Email was not confirmed.");

    when(refreshTokenRepository.findOptionalByCodeAndAvailableIsTrue(anyString()))
      .thenReturn(Optional.of(refresh));

    var exception = assertThrowsExactly(ResponseStatusException.class, () -> service.create(body));

    assertEquals("Email was not confirmed.", exception.getReason());
    assertTrue(exception.getStatusCode().isSameCodeAs(FORBIDDEN));

    verify(properties, times(0)).getTokenSecret();
    verify(properties, times(0)).getTokenExpirationInHours();
    verify(properties, times(0)).getRefreshTokenExpirationInDays();

    verify(jwt, times(0)).encode(anyString(), anyList(), any(LocalDateTime.class), anyString());
  }
}
