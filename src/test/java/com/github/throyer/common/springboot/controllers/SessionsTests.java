package com.github.throyer.common.springboot.controllers;

import static com.github.throyer.common.springboot.utils.JsonUtils.toJson;
import static com.github.throyer.common.springboot.utils.Random.randomUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import com.github.throyer.common.springboot.domain.user.repository.UserRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureDataJpa
@AutoConfigureMockMvc
public class SessionsTests {

    @Autowired
    private MockMvc api;
    
    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("Deve gerar o token quando a senha estiver correta.")
    public void should_sigh_in_with_correct_password() throws Exception {
        
        var user = randomUser();

        var email = user.getEmail();
        var password = user.getPassword();

        repository.save(user);

        var body = """
            {
                "email": "%s",
                "password": "%s"
            }
        """;

        var request = post("/api/sessions")
            .content(String.format(body, email, password))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        api.perform(request)
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Não gerar o token quando a senha estiver incorreta.")
    public void not_should_sigh_in_with_wrong_password() throws Exception {

        var user = repository.save(randomUser());
        
        var body = toJson(Map.of(
            "email", user.getEmail(),
            "password", "Írineu! você não sabe, nem eu!"
        ));

        var request = post("/api/sessions")
            .content(body)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        api.perform(request)
            .andDo(print())
            .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Não deve aceitar requisições sem o token no cabeçalho quando as rotas forem protegidas.")
    public void dont_should_accept_requests_without_token_on_header_when_authorized_route() throws Exception {
        
        var request = get("/api/users");

        api.perform(request)
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
