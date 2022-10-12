package com.github.throyer.example.modules.users.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.github.throyer.example.modules.shared.utils.HashIdsUtils;
import com.github.throyer.example.modules.shared.utils.JSON;
import com.github.throyer.example.modules.users.repositories.UserRepository;

import java.util.Map;

import static com.github.throyer.example.modules.shared.utils.JSON.stringify;
import static com.github.throyer.example.modules.shared.utils.Random.*;
import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersControllerTests {

  @Autowired
  UserRepository repository;

  @Autowired
  private MockMvc api;

  @Test
  @DisplayName("Deve salvar um novo usuário.")
  void should_save_a_new_user() throws Exception {
    var json = stringify(Map.of(
        "name", name(),
        "email", email(),
        "password", password()));

    api.perform(post("/api/users")
        .content(json)
        .header(CONTENT_TYPE, APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.id").isNotEmpty());
  }

  @Test
  @DisplayName("Deve retornar status code 400 caso faltar algum campo requerido.")
  void should_return_400_saving_user_without_required_fields() throws Exception {

    var payload = stringify(Map.of(
        "name", name(),
        "password", "123"));

    api.perform(post("/api/users")
        .content(payload)
        .header(CONTENT_TYPE, APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(greaterThan(0))));
  }

  @Test
  @DisplayName("Deve listar os usuários.")
  void should_list_users() throws Exception {

    repository.saveAll(users(4));

    api.perform(get("/api/users")
        .header(AUTHORIZATION, token("ADM"))
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
    var id = HashIdsUtils.encode(user.getId());

    var token = token(user.getId(), "ADM,USER");

    var expression = format("$.content[?(@.id == '%s')].id", id);

    var index = get("/api/users")
        .header(AUTHORIZATION, token)
        .queryParam("page", "0")
        .queryParam("size", "10");

    api.perform(index)
        .andExpect(status().isOk())
        .andExpect(jsonPath(expression, hasItem(id)));

    api.perform(delete(format("/api/users/%s", id))
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

    api.perform(delete(format("/api/users/%s", HashIdsUtils.encode(id)))
        .header(AUTHORIZATION, token(id, "ADM,USER")))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve retornar status code 404 depois de remover o usuário.")
  void should_return_404_after_delete_user() throws Exception {
    var user = repository.save(user());

    var request = delete(format("/api/users/%s", HashIdsUtils.encode(user.getId())))
        .header(AUTHORIZATION, token(user.getId(), "ADM,USER"));

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
        "password", password()));

    var request = post("/api/users")
        .content(body)
        .header(CONTENT_TYPE, APPLICATION_JSON);

    api.perform(request)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.id").isNotEmpty());

    api.perform(request)
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(1)));
  }
}