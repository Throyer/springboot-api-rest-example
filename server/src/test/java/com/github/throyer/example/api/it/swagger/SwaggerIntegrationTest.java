package com.github.throyer.example.api.it.swagger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Integration")
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
  "spring.datasource.driver-class-name=org.h2.Driver",
  "spring.datasource.url=jdbc:h2:mem:test;mode=PostgreSQL;DATABASE_TO_UPPER=false;DB_CLOSE_DELAY=-1",
  "spring.datasource.username=sa",
  "spring.datasource.password=sa",
  "swagger.username=",
  "swagger.password=",
})
class SwaggerIntegrationTest {

  @Autowired
  private MockMvc api;

  @Test
  @DisplayName("Deve exibir a documentação | swagger ui")
  void should_show_swagger_docs_ui() throws Exception {
    var request = get("/swagger-ui/index.html?configUrl=/documentation/schemas/swagger-config");
    api.perform(request)
    .andExpect(status().isOk());
  }
}
