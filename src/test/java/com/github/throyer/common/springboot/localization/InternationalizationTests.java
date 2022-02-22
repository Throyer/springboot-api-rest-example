package com.github.throyer.common.springboot.localization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
public class InternationalizationTests {

    @Autowired
    private MockMvc api;

    @Test
    @DisplayName("Deve retornar as mensagens de erro em pt BR.")
    public void should_return_error_messages_in_pt_BR() throws Exception {
        var body = "{ \"password\": \"senha_bem_segura_1234\", \"email\": \"email@email.com\" }";

        api.perform(post("/api/sessions")
                    .content(body)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "pt-BR"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Senha ou usuário invalido."));;
    }

    @Test
    @DisplayName("Deve retornar as mensagens de erro em Inglês.")
    public void should_return_error_messages_in_english() throws Exception {
        var body = "{ \"password\": \"senha_bem_segura_1234\", \"email\": \"email@email.com\" }";

        api.perform(post("/api/sessions")
                    .content(body)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.ACCEPT_LANGUAGE, "en-US"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Invalid password or username."));;
    }
}