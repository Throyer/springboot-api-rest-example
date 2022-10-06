package com.github.throyer.example.modules.authentication.controllers;

import static com.github.throyer.example.modules.shared.utils.Random.token;
import static com.github.throyer.example.modules.shared.utils.Random.user;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import com.github.throyer.example.modules.shared.utils.JSON;
import com.github.throyer.example.modules.users.repositories.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
class AuthenticationControllerTests {

  @Autowired
  private MockMvc api;

  @Autowired
  UserRepository repository;

  @Test
  @DisplayName("deve retornar OK quando a senha estiver correta")
  void should_return_OK_when_password_is_correct() throws Exception {

    var user = user();

    var body = JSON.stringify(Map.of(
        "email", user.getEmail(),
        "password", user.getPassword()));

    repository.save(user);

    api.perform(post("/api/v1/authentication")
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

    api.perform(post("/api/v1/authentication")
        .content(body)
        .header(CONTENT_TYPE, APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("deve retornar FORBIDDEN quando o usuário não existir")
  void should_return_FORBIDDEN_when_user_does_not_exist() throws Exception {
    var body = JSON.stringify(Map.of(
        "email", "this.not.exist@email.com",
        "password", "Írineu! você não sabe, nem eu!"));

    api.perform(post("/api/v1/authentication")
        .content(body)
        .header(CONTENT_TYPE, APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("não deve aceitar requisições sem o token no cabeçalho quando as rotas forem privadas")
  void should_not_accept_requests_without_token_in_header_when_routes_are_private() throws Exception {
    api.perform(get("/api/users"))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("Can't find bearer token on Authorization header."));
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token expirado")
  void should_not_accept_requests_with_token_expired() throws Exception {
    var expiredToken = token(now().minusHours(24), "ADM");

    api.perform(get("/api/users")
        .header(AUTHORIZATION, expiredToken))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("Token expired or invalid."));
  }

  @Test
  @DisplayName("deve aceitar requisições com o token um válido")
  void should_accept_requests_with_token_valid() throws Exception {
    var token = token("ADM");

    api.perform(get("/api/v1/users")
        .header(AUTHORIZATION, token))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token um inválido")
  void must_not_accept_requests_with_an_invalid_token() throws Exception {
    var token = token(now().plusHours(24), "ADM", "this_is_not_my_secret");

    api.perform(get("/api/users")
        .header(AUTHORIZATION, token))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("Token expired or invalid."));
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token usando um hash inválido")
  void must_not_accept_requests_with_an_invalid_hash_token() throws Exception {
    var token = "Bearer abububle_das_ideia";

    api.perform(get("/api/users")
        .header(AUTHORIZATION, token))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("Token expired or invalid."));
  }

  @Test
  @DisplayName("não deve aceitar requisições com o token sem a role correta")
  void must_not_accept_requests_with_token_without_the_correct_role() throws Exception {
    var token = token("THIS_IS_NOT_CORRECT_ROLE");

    api.perform(get("/api/v1/users")
        .header(AUTHORIZATION, token))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Not authorized."));
  }

  @Test
  @DisplayName("deve aceitar requisições sem token em rotas publicas")
  void must_accept_requests_without_token_on_public_routes() throws Exception {
    api.perform(get("/api"))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("deve aceitar requisições sem token em rotas publicas porem com um token invalido no header")
  void must_accept_requests_without_token_on_public_routes_but_with_invalid_token_on_header() throws Exception {
    api.perform(get("/api").header(AUTHORIZATION, token("ADM", "this_is_not_my_secret")))
        .andExpect(status().isOk());
  }
}
