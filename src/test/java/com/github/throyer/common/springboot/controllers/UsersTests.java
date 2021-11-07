package com.github.throyer.common.springboot.controllers;

import static com.github.throyer.common.springboot.utils.JsonUtils.toJson;
import static com.github.throyer.common.springboot.utils.Random.FAKER;
import static com.github.throyer.common.springboot.utils.Random.password;
import static com.github.throyer.common.springboot.utils.Random.randomUser;
import static com.github.throyer.common.springboot.utils.TokenUtils.token;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import com.github.throyer.common.springboot.domain.repositories.UserRepository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsersTests {

    private String header;

    @Value("${token.secret}")
    private String TOKEN_SECRET;

    @Value("${token.expiration-in-hours}")
    private Integer TOKEN_EXPIRATION_IN_HOURS;

    @Autowired
    UserRepository repository;

    @Autowired
    private MockMvc api;

    @BeforeAll
    public void generateToken() {
        this.header = token(TOKEN_EXPIRATION_IN_HOURS, TOKEN_SECRET);
    }

    @Test
    @DisplayName("Deve salvar um novo usuário.")
    public void should_save_a_new_user() throws Exception {

        var json = toJson(Map.of(
            "name", FAKER.name().fullName(),
            "email", FAKER.internet().safeEmailAddress(),
            "password", password()
        ));

        var request = post("/api/users")
            .content(json)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        api.perform(request)
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.id").isNotEmpty());
    }
    
    @Test
    @DisplayName("Deve retornar status code 400 caso faltar algum campo requerido.")
    public void should_return_400_saving_user_without_required_fields() throws Exception {

        var payload = toJson(Map.of(
            "name", FAKER.name().fullName(),
            "password", "123"
        ));
        
        var request = post("/api/users")
            .content(payload)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        api.perform(request)
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("Deve listar os usuários.")
    public void should_list_users() throws Exception {

        repository.saveAll(List.of(randomUser(), randomUser(), randomUser(), randomUser()));
        
        var request = get("/api/users")
            .header(HttpHeaders.AUTHORIZATION, header)
                .queryParam("page", "0")
                .queryParam("size", "10");

        api.perform(request).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(greaterThan(3))));
    }

    @Test
    @DisplayName("Deve deletar usuário.")
    public void should_delete_user() throws Exception {

        var user = repository.save(randomUser());
        
        var request = delete(String.format("/api/users/%s", user.getId()))
            .header(HttpHeaders.AUTHORIZATION, header);

        api.perform(request).andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status code 404 depois de remover o usuário.")
    public void should_return_404_after_delete_user() throws Exception {  
        var user = repository.save(randomUser());

        var fist = delete(String.format("/api/users/%s", user.getId()))
            .header(HttpHeaders.AUTHORIZATION, header);

        api.perform(fist).andDo(print())
                .andExpect(status().isNoContent());

        var second = delete(String.format("/api/users/%s", user.getId()))
            .header(HttpHeaders.AUTHORIZATION, header);

        api.perform(second).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar status code 400 quando salvar um usuário com o mesmo email.")
    public void should_return_400_after_save_same_email() throws Exception {  

        var json = toJson(Map.of(
            "name", FAKER.name().fullName(),
            "email", FAKER.internet().safeEmailAddress(),
            "password", password()
        ));

        var first = post("/api/users")
            .content(json)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        api.perform(first)
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.id").isNotEmpty());

        var second = post("/api/users")
            .content(json)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        api.perform(second)
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(1)));
    }
}