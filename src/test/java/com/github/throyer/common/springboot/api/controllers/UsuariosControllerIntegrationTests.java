package com.github.throyer.common.springboot.api.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Permissao;
import com.github.throyer.common.springboot.api.models.entity.Usuario;
import com.github.throyer.common.springboot.api.models.security.Authorized;
import com.github.throyer.common.springboot.api.repositories.UsuarioRepository;
import com.github.throyer.common.springboot.api.services.TokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UsuariosControllerIntegrationTests {
    
    private String bearerToken;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private MockMvc mock;

    @BeforeEach
    public void generateToken() {
        var permissoes = List.of(new Permissao("ADMINISTRADOR"));
        var user = new Authorized("ADMINISTRADOR", 1L, permissoes);
        bearerToken = String.format("Bearer %s", tokenService.buildToken(user));
    }

    @Test
    public void salvar_usuario_sem_campos_obrigatorios_deve_retornar_400() throws Exception {

        String payload = "{\"nome\":\"fulaninho\", \"senha\": \"123\"}";
        
        var request = post("/usuarios")
            .content(payload)
                .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        mock.perform(request)
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void deve_listar_os_usuarios() throws Exception {

        repository.saveAll(List.of(
            new Usuario("Renatinho", "renatinho@email.com", "1232", new ArrayList<>()),
            new Usuario("fulano", "fulano@email.com", "1232", new ArrayList<>()),
            new Usuario("cicrano", "cicrano@email.com", "1232", new ArrayList<>())
        ));
        
        var request = get("/usuarios")
            .header(HttpHeaders.AUTHORIZATION, bearerToken)
                .queryParam("page", "0")
                .queryParam("size", "10");

        mock.perform(request).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(4)));
    }
    
}