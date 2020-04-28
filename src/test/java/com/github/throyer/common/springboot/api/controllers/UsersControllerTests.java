package com.github.throyer.common.springboot.api.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Permissao;
import com.github.throyer.common.springboot.api.models.entity.Usuario;
import com.github.throyer.common.springboot.api.models.security.Authorized;
import com.github.throyer.common.springboot.api.repositories.UsuarioRepository;
import com.github.throyer.common.springboot.api.services.TokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Sql({ "classpath:usuarios.sql" })
public class UsersControllerTests {
    
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private MockMvc mock;

    @BeforeEach
    public void beforeEach() {
        // before each
    }

    /**
     * Salvar um usuarios sem os campos obrigatorios deve retornar 400 Bad Request.Body:
    <code>
    [
        {
            "campo": "permissoes",
            "messagem": "não pode ser nulo"
        },
        {
            "campo": "email",
            "messagem": "O e-mail não pode ser NULL."
        },
        {
            "campo": "senha",
            "messagem": "No mínimo 8 caracteres, com no mínimo um número, um caractere especial, uma letra maiúscula e uma letra minúscula."
        }
    ]
    </code>
     * @throws java.lang.Exception
     */
    @Test
    public void salvar_usuario_sem_campos_obrigatorios_deve_retornar_400() throws Exception {

        String usuario = "{\"nome\":\"fulaninho\", \"senha\": \"123\"}";
        
        mock.perform(post("/usuarios")
                .content(usuario)
                .header(HttpHeaders.AUTHORIZATION, getToken())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isArray());
    }   
    
    @Test
    public void deve_listar_todos_os_usuarios() throws Exception {
        mock.perform(get("/usuarios")
            .header(HttpHeaders.AUTHORIZATION, getToken())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.content", hasSize(3)));
    }

    private String getToken() {
        var permissoes = List.of(new Permissao("ADMINISTRADOR"));
        var user = new Authorized("ADMINISTRADOR", 1L, permissoes);
        return String.format("Bearer %s", tokenService.buildToken(user));
    }
    
}
