package com.github.throyer.example.api.it.security;

import com.github.throyer.example.api.domain.user.persistence.repositories.springdata.SpringDataUserRepository;
import com.github.throyer.example.api.infra.environments.SecurityProperties;
import com.github.throyer.example.api.utils.JSON;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static com.github.throyer.example.api.fixtures.TokenFixture.token;
import static com.github.throyer.example.api.fixtures.UserFixture.user;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Integration")
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@TestPropertySource(properties = {
  "spring.jpa.show-sql=false"
})
public class JsonWebTokenIntegrationTest {

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
    .withDatabaseName("users")
    .withUsername("root")
    .withPassword("root");

  @Autowired
  private MockMvc api;

  @Autowired
  SpringDataUserRepository repository;

  @Autowired
  SecurityProperties security;

  @Test
  @DisplayName("deve retornar OK quando a senha estiver correta")
  void should_return_OK_when_password_is_correct() throws Exception {

    var user = user();

    var body = JSON.stringify(Map.of(
      "email", user.getEmail(),
      "password", user.getPassword()
    ));

    repository.save(user);

    api.perform(post("/authentication")
      .content(body)
      .header(CONTENT_TYPE, APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @DisplayName("deve retornar FORBIDDEN quando a senha estiver incorreta")
  void should_return_FORBIDDEN_when_password_is_incorrect() throws Exception {
    var user = repository.save(user());

    var body = JSON.stringify(Map.of(
    "email", user.getEmail(),
    "password", "Írineu! você não sabe, nem eu!"));

    api.perform(post("/authentication")
      .content(body)
      .header(CONTENT_TYPE, APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("deve retornar FORBIDDEN quando o usuário não existir")
  void should_return_FORBIDDEN_when_user_does_not_exist() throws Exception {
    var body = JSON.stringify(Map.of(
      "email", "this.not.exist@email.com",
      "password", "Írineu! você não sabe, nem eu!"
    ));

    api.perform(post("/authentication")
      .content(body)
      .header(CONTENT_TYPE, APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("não deve aceitar requisições sem o token no cabeçalho quando as rotas forem privadas")
  void should_not_accept_requests_without_token_in_header_when_routes_are_private() throws Exception {
    api.perform(get("/users"))
    .andExpect(status().isForbidden())
    .andExpect(jsonPath("$.message").value("Can't find bearer token on Authorization header."));
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token expirado")
  void should_not_accept_requests_with_token_expired() throws Exception {
    var expiredToken = token(now().minusHours(24), "ADM", security.getTokenSecret());

    api.perform(get("/users")
    .header(AUTHORIZATION, expiredToken))
    .andExpect(status().isForbidden())
    .andExpect(jsonPath("$.message").value("Token expired or invalid."));
  }

  @Test
  @DisplayName("deve aceitar requisições com o token um válido")
  void should_accept_requests_with_token_valid() throws Exception {
    var token = token("ADM", security.getTokenSecret());

    api.perform(get("/users")
    .header(AUTHORIZATION, token))
    .andExpect(status().isOk())
    .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token um inválido")
  void must_not_accept_requests_with_an_invalid_token() throws Exception {
    var token = token(now().plusHours(24), "ADM", "this_is_not_my_token_secret");

    api.perform(get("/users")
    .header(AUTHORIZATION, token))
    .andExpect(status().isForbidden())
    .andExpect(jsonPath("$.message").value("Token expired or invalid."));
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token usando um hash inválido")
  void must_not_accept_requests_with_an_invalid_hash_token() throws Exception {
    var token = "Bearer abububle_das_ideia";

    api.perform(get("/users")
    .header(AUTHORIZATION, token))
    .andExpect(status().isForbidden())
    .andExpect(jsonPath("$.message").value("Token expired or invalid."));
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token sem a role correta")
  void must_not_accept_requests_with_token_without_the_correct_role() throws Exception {
    var token = token("THIS_IS_NOT_CORRECT_ROLE", security.getTokenSecret());

    api.perform(get("/users")
    .header(AUTHORIZATION, token))
    .andExpect(status().isUnauthorized())
    .andExpect(jsonPath("$.message").value("Not authorized."));
  }

  @Test
  @DisplayName("deve aceitar requisições sem token em rotas publicas")
  void must_accept_requests_without_token_on_public_routes() throws Exception {
    api.perform(get("/"))
    .andExpect(status().isOk());
  }

  @Test
  @DisplayName("deve aceitar requisições sem token em rotas publicas porem com um token invalido no header")
  void must_accept_requests_without_token_on_public_routes_but_with_invalid_token_on_header() throws Exception {
    api.perform(get("/").header(AUTHORIZATION, token("ADM", "this_is_not_my_token_secret")))
    .andExpect(status().isOk());
  }
}
