package com.github.throyer.example.api.it.user;

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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static com.github.throyer.example.api.fixtures.TokenFixture.token;
import static com.github.throyer.example.api.fixtures.UserFixture.*;
import static com.github.throyer.example.api.utils.ID.encode;
import static com.github.throyer.example.api.utils.Random.FAKER;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Integration")
@Transactional
@Testcontainers
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
  "spring.jpa.show-sql=false"
})
public class UsersApiIntegrationTest {
  @Autowired
  SpringDataUserRepository repository;
  
  @Autowired
  SecurityProperties security;

  @Autowired
  private MockMvc api;
  
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
    .withDatabaseName("users")
    .withUsername("root")
    .withPassword("root");

  @Test
  @DisplayName("Deve salvar um novo usuário.")
  void should_save_a_new_user() throws Exception {
    var json = JSON.stringify(Map.of(
      "name", name(),
      "email", email(),
      "password", password()
    ));

    api.perform(post("/users")
        .content(json)
        .header(CONTENT_TYPE, APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.id").isNotEmpty());
  }

  @Test
  @DisplayName("Deve retornar status code 400 caso faltar algum campo requerido.")
  void should_return_400_saving_user_without_required_fields() throws Exception {

    var payload = JSON.stringify(Map.of(
      "name", name(),
      "password", "123"
    ));

    api.perform(post("/users")
        .content(payload)
        .header(CONTENT_TYPE, APPLICATION_JSON))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.errors").isArray())
      .andExpect(jsonPath("$.errors", hasSize(greaterThan(0))));
  }

  @Test
  @DisplayName("Deve listar os usuários.")
  void should_list_users() throws Exception {

    repository.saveAll(users(4));

    api.perform(get("/users")
        .header(AUTHORIZATION, token("ADM", security.getTokenSecret()))
        .queryParam("page", "0")
        .queryParam("size", "10"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.content").isArray())
      .andExpect(jsonPath("$.content", hasSize(greaterThan(3))));
  }

  @Test
  @DisplayName("Must not list deleted users.")
  void must_not_list_deleted_users() throws Exception {

    var user = repository.save(user());
    var id = encode(user.getId());

    var token = token(user.getId(), "ADM,USER", security.getTokenSecret());

    var expression = format("$.content[?(@.id == '%s')].id", id);

    var index = get("/users")
      .header(AUTHORIZATION, token)
      .queryParam("page", "0")
      .queryParam("size", "10");

    api.perform(index)
      .andExpect(status().isOk())
      .andExpect(jsonPath(expression, hasItem(id)));

    api.perform(delete(format("/users/%s", id))
        .header(AUTHORIZATION, token))
      .andExpect(status().isNoContent());

    api.perform(index)
      .andExpect(status().isOk())
      .andExpect(jsonPath(expression).isEmpty());
  }

  @Test
  @DisplayName("Deve deletar usuário.")
  void should_delete_user() throws Exception {
    var user = repository.save(user());
    var id = user.getId();

    api.perform(delete(format("/users/%s", encode(id)))
        .header(AUTHORIZATION, token(id, "ADM,USER", security.getTokenSecret())))
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve retornar status code 404 depois de remover o usuário.")
  void should_return_404_after_delete_user() throws Exception {
    var user = repository.save(user());

    var request = delete(format("/users/%s", encode(user.getId())))
      .header(AUTHORIZATION, token(user.getId(), "ADM,USER", security.getTokenSecret()));

    api.perform(request)
      .andExpect(status().isNoContent());

    api.perform(request)
      .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("Deve retornar status code 400 quando salvar um usuário com o mesmo email.")
  void should_return_400_after_save_same_email() throws Exception {

    var body = JSON.stringify(Map.of(
      "name", FAKER.name().fullName(),
      "email", FAKER.internet().safeEmailAddress(),
      "password", password()
    ));

    var request = post("/users")
      .content(body)
      .header(CONTENT_TYPE, APPLICATION_JSON);

    api.perform(request)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").exists())
      .andExpect(jsonPath("$.id").isNotEmpty());

    api.perform(request)
      .andExpect(status().isConflict())
      .andExpect(jsonPath("$").isMap())
      .andExpect(jsonPath("$.message").value("e-mail unavailable."));
  }
}
