package com.github.throyer.common.springboot.api.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Permissao;
import com.github.throyer.common.springboot.api.models.entity.Usuario;
import com.github.throyer.common.springboot.api.models.security.Authorized;
import com.github.throyer.common.springboot.api.repositories.UsuarioRepository;
import com.github.throyer.common.springboot.api.services.TokenService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.ContentResultMatchers;

import springfox.documentation.service.MediaTypes;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql({ "classpath:usuarios.sql" })
public class UsersControllerTests {
    
    @Autowired
    private TokenService tokenService;

    @MockBean
    private UsuarioRepository repository;

    @Autowired
    private MockMvc mock;

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
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)));

        verify(repository, times(0)).save(any(Usuario.class));
    }

    @Test
    public void deve_listar_os_usuarios() throws Exception {

        var u1 = new Usuario();
        u1.setNome("Renatinho");
        u1.setEmail("renatinho@email.com");
        u1.setSenha("1232");

        var u2 = new Usuario();
        u2.setNome("fulano");
        u2.setEmail("fulano@email.com");
        u2.setSenha("1232");

        var u3 = new Usuario();
        u3.setNome("cicrano");
        u3.setEmail("cicrano@email.com");
        u3.setSenha("1232");

        var usuarios = List.of(u1, u2, u3);

        repository.saveAll(usuarios);
        
        var request = get("/usuarios")
            .header(HttpHeaders.AUTHORIZATION, getToken())
                .queryParam("page", "0")
                .queryParam("size", "10");

        var result = mock.perform(request);
        
        result
            .andDo(print())
                .andExpect(status().isOk());
                // .andExpect(jsonPath("$.content").isArray())
                // .andExpect(jsonPath("$.content", hasSize(6)));
    }

    private String getToken() {
        var permissoes = List.of(new Permissao("ADMINISTRADOR"));
        var user = new Authorized("ADMINISTRADOR", 1L, permissoes);
        return String.format("Bearer %s", tokenService.buildToken(user));
    }
    
}
