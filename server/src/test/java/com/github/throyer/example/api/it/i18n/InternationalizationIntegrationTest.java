package com.github.throyer.example.api.it.i18n;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.HttpHeaders.ACCEPT_LANGUAGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Integration")
@Transactional
@Testcontainers
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = MOCK)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
  "spring.jpa.show-sql=false",
  "spring.datasource.driver-class-name=org.h2.Driver",
  "spring.datasource.url=jdbc:h2:mem:test;mode=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
  "spring.datasource.username=sa",
  "spring.datasource.password=sa",
})
public class InternationalizationIntegrationTest {
  @Autowired
  private MockMvc api;

  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
    .withDatabaseName("users")
    .withUsername("root")
    .withPassword("root");

  @Test
  @DisplayName("Deve retornar as mensagens de erro em pt BR.")
  void should_return_error_messages_in_pt_BR() throws Exception {
    var body = "{ \"password\": \"senha_bem_segura_1234\", \"email\": \"email@email.com\" }";

    api.perform(post("/authentication")
        .content(body)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(ACCEPT_LANGUAGE, "pt-BR"))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.message").value("Nome de usuário ou senha inválidos."));
  }

  @Test
  @DisplayName("Deve retornar as mensagens de erro em Inglês.")
  void should_return_error_messages_in_english() throws Exception {
    var body = "{ \"password\": \"senha_bem_segura_1234\", \"email\": \"email@email.com\" }";

    api.perform(post("/authentication")
        .content(body)
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(ACCEPT_LANGUAGE, "en-US"))
      .andExpect(status().isForbidden())
      .andExpect(jsonPath("$.message").value("Invalid password or username."));
    ;
  }
}
