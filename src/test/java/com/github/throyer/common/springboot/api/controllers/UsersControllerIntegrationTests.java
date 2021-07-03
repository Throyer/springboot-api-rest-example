package com.github.throyer.common.springboot.api.controllers;

import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.JWT;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import com.github.throyer.common.springboot.api.domain.builders.UserBuilder;
import com.github.throyer.common.springboot.api.domain.models.entity.Role;
import com.github.throyer.common.springboot.api.domain.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureDataJpa
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class UsersControllerIntegrationTests {

    private String header;

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    @Value("${token.expiration-in-hours}")
    private Integer TOKEN_EXPIRATION_IN_HOURS;

    @Autowired
    UserRepository repository;

    @Autowired
    private MockMvc mock;

    @BeforeEach
    public void generateToken() {
        var expiration = LocalDateTime.now().plusHours(TOKEN_EXPIRATION_IN_HOURS);
        var token = JWT.encode(1L, List.of(new Role("ADM")), expiration, TOKEN_SECRET);
        header = String.format("Bearer %s", token);
    }

    @Test
    public void should_save_a_new_user() throws Exception {
        var json = """
            {
                \"name\": \"novo usuário\",
                \"email\": \"novo.usuario@email.com\",
                \"password\": \"uma_senha_123@SEGURA\"
            }
        """;

        var request = post("/users")
            .content(json)
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
            .header(HttpHeaders.AUTHORIZATION, header)
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
            .header(HttpHeaders.AUTHORIZATION, header);

        mock.perform(request).andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_404_after_delete_user() throws Exception {  
        var user = repository.save(
            new UserBuilder("edinaldo pereira")
                .setEmail("edinaldo@email.com")
                .addRole(2L)
                .setPassword("uma_senha_123@SEGURA")
                .build()
        );

        var fist = delete(String.format("/users/%s", user.getId()))
            .header(HttpHeaders.AUTHORIZATION, header);

        mock.perform(fist).andDo(print())
                .andExpect(status().isNoContent());

        var second = delete(String.format("/users/%s", user.getId()))
            .header(HttpHeaders.AUTHORIZATION, header);

        mock.perform(second).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void should_return_400_after_save_same_email() throws Exception {  
        var json = """
            {
                \"name\": \"novo usuário\",
                \"email\": \"user@email.com\",
                \"password\": \"uma_senha_123@SEGURA\"
            }
        """;

        var first = post("/users")
            .content(json)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mock.perform(first)
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.id").isNotEmpty());

        var second = post("/users")
            .content(json)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mock.perform(second)
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(1)));
    }
}