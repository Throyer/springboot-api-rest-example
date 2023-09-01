package com.github.throyer.example.api.domain.authentication.services;

import com.github.throyer.example.api.domain.authentication.models.Authorized;
import com.github.throyer.example.api.domain.user.persistence.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repository.findOptionalByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Senha ou usuário invalido."));
        return new Authorized(user);
    }
}
