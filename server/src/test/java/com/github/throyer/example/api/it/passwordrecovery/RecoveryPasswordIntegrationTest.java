package com.github.throyer.example.api.it.passwordrecovery;

import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import com.github.throyer.example.api.infra.mail.services.EMailService;
import com.github.throyer.example.api.shared.mail.models.Email;
import com.github.throyer.example.api.utils.JSON;
import com.github.throyer.example.api.utils.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static com.github.throyer.example.api.fixtures.RoleFixture.roles;
import static com.github.throyer.example.api.fixtures.UserFixture.password;
import static com.github.throyer.example.api.fixtures.UserFixture.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.utility.DockerImageName.parse;

@Tag("Integration")
@Transactional
@Testcontainers
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = MOCK)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {
  "spring.jpa.show-sql=false"
})
public class RecoveryPasswordIntegrationTest {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MockMvc api;
  
  @MockBean
  private EMailService eMailService;
  
  @Container
  @ServiceConnection
  static PostgreSQLContainer<?> postgresql = new PostgreSQLContainer<>(parse("postgres:13"))
    .withDatabaseName("users")
    .withUsername("root")
    .withPassword("root");

  @Test
  @DisplayName("Deve responder No Content quando email for invalido.")
  void should_return_no_content_on_invalid_email() throws Exception {
    doNothing()
      .when(eMailService)
        .send(any(Email.class));

    var json = JSON.stringify(Map.of(
      "email", "jubileu.da.silva@email.fake.com"
    ));

    api.perform(
        post("/recoveries")
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .content(json))
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve responder No Content quando email for valido.")
  void should_return_no_content_on_valid_email() throws Exception {
    doNothing()
      .when(eMailService)
        .send(any(Email.class));
    
    var user = user();

    userRepository.save(user);

    var json = JSON.stringify(Map.of(
      "email", user.getEmail()));

    api.perform(
        post("/recoveries")
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .content(json))
      .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("Deve confirmar o código quando ele for valido for valido.")
  void should_confirm_code_when_is_valid() throws Exception {
    doNothing()
      .when(eMailService)
        .send(any(Email.class));
    
    var user = user(
      2L,
      "fulaninho",
      "fake@email.com",
      password(),
      roles()
    );
    
    userRepository.save(user);

    var recovery = JSON.stringify(Map.of(
      "email", user.getEmail()
    ));

    try (MockedStatic<Random> randomMockedStatic = mockStatic(Random.class)) {
      randomMockedStatic.when(Random::code).thenReturn("1366");

      api.perform(
          post("/recoveries")
            .header(CONTENT_TYPE, APPLICATION_JSON)
            .content(recovery))
        .andExpect(status().isNoContent());
    }
    
    var confirm = JSON.stringify(Map.of(
      "email", user.getEmail(),
      "code", "1366"
    ));

    api.perform(
        post("/recoveries/confirm")
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .content(confirm))
      .andExpect(status().isNoContent());
    
    var update = JSON.stringify(Map.of(
      "email", user.getEmail(),
      "code", "1366",
      "password", "uma_nova_senha_que_definitivamente_é_bem_grande"
    ));

    api.perform(
        post("/recoveries/update")
          .header(CONTENT_TYPE, APPLICATION_JSON)
          .content(update))
      .andExpect(status().isNoContent());

    var body = JSON.stringify(Map.of(
      "email", user.getEmail(),
      "password", "uma_nova_senha_que_definitivamente_é_bem_grande"
    ));

    api.perform(post("/authentication")
        .content(body)
        .header(CONTENT_TYPE, APPLICATION_JSON))
      .andExpect(status().isOk());
  }
}
