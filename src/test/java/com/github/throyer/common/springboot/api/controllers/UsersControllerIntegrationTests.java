package com.github.throyer.common.springboot.api.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.github.throyer.common.springboot.api.builders.UserBuilder;
import com.github.throyer.common.springboot.api.models.entity.Role;
import com.github.throyer.common.springboot.api.models.security.Authorized;
import com.github.throyer.common.springboot.api.repositories.UserRepository;
import com.github.throyer.common.springboot.api.services.TokenService;

import org.junit.jupiter.api.BeforeEach;
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
public class UsersControllerIntegrationTests {

    private String bearerToken;

    @Autowired
    private TokenService tokenService;

    @Autowired
    UserRepository repository;

    @Autowired
    private MockMvc mock;

    @BeforeEach
    public void generateToken() {
        final var roles = List.of(new Role("ADM"));
        final var user = new Authorized("ADM", 1L, roles);
        bearerToken = String.format("Bearer %s", tokenService.buildToken(user));
    }

    @Test
    public void should_save_a_new_user() throws Exception {
        var json = """
            {
                \"name\": \"novo usuário\",
                \"email\": \"novo.usuario2@email.com\",
                \"password\": \"uma_senha_123@SEGURA\",
                \"roles\": [
                    {
                        \"id\": 2
                    }
                ]
            }
        """;

        var request = post("/users")
            .content(json)
            .header(HttpHeaders.AUTHORIZATION, bearerToken)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.id").isNotEmpty());
    }
    
    @Test
    public void should_return_400_saving_user_without_required_fields() throws Exception {

        String payload = """
            {
                \"name\":\"fulaninho\",
                \"password\": \"123\"
            }
        """;
        
        var request = post("/users")
            .content(payload)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void should_list_users() throws Exception {        
        var request = get("/users")
            .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .queryParam("page", "0")
                .queryParam("size", "10");

        mock.perform(request).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    public void should_delete_user() throws Exception {  
        var user = repository.save(
            new UserBuilder("novo usuário")
                .setEmail("novo@email.com")
                .addRole(2L)
                .setPassword("uma_senha_123@SEGURA")
                .build()
            );
        
        var request = delete(String.format("/users/%s", user.getId()))
            .header(HttpHeaders.AUTHORIZATION, bearerToken);

        mock.perform(request).andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404_after_delete_user() throws Exception {  
        var user = repository.save(
            new UserBuilder("novo usuário")
                .setEmail("novo@email.com")
                .addRole(2L)
                .setPassword("uma_senha_123@SEGURA")
                .build()
            );
        
        var request = delete(String.format("/users/%s", user.getId()))
            .header(HttpHeaders.AUTHORIZATION, bearerToken);

        mock.perform(request).andDo(print())
                .andExpect(status().isNoContent());

        var tryAgainRequest = delete(String.format("/users/%s", user.getId()))
        .header(HttpHeaders.AUTHORIZATION, bearerToken);

        mock.perform(tryAgainRequest).andDo(print())
                .andExpect(status().isNotFound());
    }
}