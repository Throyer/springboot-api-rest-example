package com.github.throyer.common.springboot.api.services;

import java.util.Optional;

import com.github.throyer.common.springboot.api.models.security.Authorized;
import com.github.throyer.common.springboot.api.repositories.UsuarioRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

    @Autowired
    UsuarioRepository repository;

    private Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private static final String NOME_USUARIO_IVALIDO = "Nome de usuario invalido.";
    private static final String NAO_EXISTE_USUARIO_LOGADO = "Não existe um usuario logado.";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new Authorized(repository.findOptionalByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(NOME_USUARIO_IVALIDO)));
    }

    public Optional<Authorized> getAuthorized() {
        
        var principal = getPrincipal();
        
        if (principal instanceof Authorized) {
            return Optional.of((Authorized) principal);
        }

        logger.error(NAO_EXISTE_USUARIO_LOGADO);
        return Optional.empty();
    }

    private Object getPrincipal() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
                .getPrincipal();
    } 
}