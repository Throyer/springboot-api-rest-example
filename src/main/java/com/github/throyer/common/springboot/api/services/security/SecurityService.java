package com.github.throyer.common.springboot.api.services.security;

import java.util.Objects;
import java.util.Optional;

import com.github.throyer.common.springboot.api.models.security.Authorized;
import com.github.throyer.common.springboot.api.repositories.UserRepository;

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
    UserRepository repository;

    private static Logger logger = LoggerFactory.getLogger(SecurityService.class);

    private static final String INVALID_USERNAME = "Nome de usuário invalido.";
    private static final String NO_SESSION_MESSAGE = "Não existe um usuário logado.";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new Authorized(repository.findOptionalByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME)));
    }

    public static Optional<Authorized> authorized() {
        
        var principal = getPrincipal();
        
        if (Objects.nonNull(principal) && principal instanceof Authorized authorized) {
            return Optional.of(authorized);
        }

        logger.error(NO_SESSION_MESSAGE);
        return Optional.empty();
    }

    private static Object getPrincipal() {
        var authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (Objects.nonNull(authentication)) {
            return authentication.getPrincipal();
        }
        return null;
    } 
}