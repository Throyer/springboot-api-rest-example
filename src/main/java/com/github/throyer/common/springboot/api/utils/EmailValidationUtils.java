package com.github.throyer.common.springboot.api.utils;

import java.util.List;

import com.github.throyer.common.springboot.api.models.entity.Usuario;
import com.github.throyer.common.springboot.api.models.validation.EmailNotUniqueException;
import com.github.throyer.common.springboot.api.models.validation.SimpleError;
import com.github.throyer.common.springboot.api.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidationUtils {

    private static UsuarioRepository repository;

    private static String CAMPO = "email";
    private static String MENSAGEM = "Este email já foi utilizado por outro usuario. Por favor utilize um email diferente.";

    private static final List<SimpleError> ERRO_EMAIL = List.of(new SimpleError(CAMPO, MENSAGEM));

    @Autowired
    public EmailValidationUtils(UsuarioRepository repository) {
        EmailValidationUtils.repository = repository;
    }

    public static void validarUnicidadeDoEmailparaNovoUsuario(Usuario usuario) {
        if (repository.existsByEmail(usuario.getEmail())) {
            throw new EmailNotUniqueException(ERRO_EMAIL);
        }
    }

    public static void validarUnicidadeDoEmailParaEdicaoDeUsuario(Usuario novo, Usuario atual) {

        var novoEmail = novo.getEmail();
        var emailAtual = atual.getEmail();

        var mudouDeEmail = !emailAtual.equals(novoEmail);

        var jaEhUsadoPorOutroUsuario = repository.existsByEmail(novoEmail);

        if (mudouDeEmail && jaEhUsadoPorOutroUsuario) {
            throw new EmailNotUniqueException(ERRO_EMAIL);
        }
    }
}