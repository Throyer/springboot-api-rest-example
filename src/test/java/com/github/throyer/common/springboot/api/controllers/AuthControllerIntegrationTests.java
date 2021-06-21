package com.github.throyer.common.springboot.api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.throyer.common.springboot.api.domain.repositories.UserRepository;

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
public class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mock;
    
    @Autowired
    UserRepository repository;

    @Test
    public void should_sigh_in_with_correct_password() throws Exception {
        
        var body = """
            {
                \"email\": \"admin@email.com\",
                \"password\": \"admin\"
            }
        """;

        var request = post("/auth/token")
            .content(body)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void dont_should_sigh_in_with_wrong_password() throws Exception {
        
        var body = """
            {
                \"email\": \"admin@email.com\",
                \"password\": \"jubileu, você não sabe. Nem eu.\"
            }
        """;

        var request = post("/auth/token")
            .content(body)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andDo(print())
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void dont_should_accept_requests_without_token_on_header() throws Exception {
        
        var request = get("/users");

        mock.perform(request)
            .andDo(print())
            .andExpect(status().isForbidden());
    }
}
