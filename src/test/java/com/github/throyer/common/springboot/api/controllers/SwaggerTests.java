package com.github.throyer.common.springboot.api.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
public class SwaggerTests {
    
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate template;

    @Test
    @DisplayName("Deve listar a documentação")
    public void should_list_swagger_docs() throws Exception {

        var url = String.format("http://localhost:%s/api/documentation/swagger-ui/#/", port);

        assertThat(template.getForObject(url, String.class).concat("Common API"));
    }
}
