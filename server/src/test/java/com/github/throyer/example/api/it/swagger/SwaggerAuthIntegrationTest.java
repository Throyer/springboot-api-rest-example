package com.github.throyer.example.api.it.swagger;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@Tag("Integration")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
  "swagger.username=test",
  "swagger.password=test",
  "spring.datasource.driver-class-name=org.h2.Driver",
  "spring.datasource.url=jdbc:h2:mem:test;mode=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
  "spring.datasource.username=sa",
  "spring.datasource.password=sa",
})
class SwaggerAuthIntegrationTest {

  @Autowired
  private MockMvc api;

  @Test
  @DisplayName("Deve exibir a documentação com basic auth valido")
  void should_display_the_swagger_docs_ui_with_valid_credentials() throws Exception {

    var request = get("/swagger-ui/index.html?configUrl=/documentation/schemas/swagger-config")
    .header(AUTHORIZATION, "Basic dGVzdDp0ZXN0");

    api.perform(request)
    .andExpect(status().isOk());
  }

  @Test
  @DisplayName("Não deve exibir a documentação com basic auth invalido")
  void must_not_display_the_swagger_docs_ui_with_invalid_credentials() throws Exception {
    var request = get("/swagger-ui/index.html?configUrl=/documentation/schemas/swagger-config")
    .header(AUTHORIZATION, "Basic anViaWxldTppcmluZXU=");

    api.perform(request)
    .andExpect(status().isUnauthorized());
  }
}
