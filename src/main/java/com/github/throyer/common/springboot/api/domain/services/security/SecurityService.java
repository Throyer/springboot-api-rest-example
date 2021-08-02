package com.github.throyer.common.springboot.api.domain.services.security;

import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.INVALID_USERNAME;
import static com.github.throyer.common.springboot.api.utils.Constants.SECURITY.JWT;

import java.util.Objects;
import java.util.Optional;

import com.github.throyer.common.springboot.api.domain.models.security.Authorized;
import com.github.throyer.common.springboot.api.domain.repositories.UserRepository;
import com.github.throyer.common.springboot.api.domain.validation.TokenExpiredOrInvalidException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

    @Autowired
    UserRepository repository;

    private static String SECRET;

    public SecurityService(@Value("${token.secret}") String secret) {
        SecurityService.SECRET = secret;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new Authorized(repository.findOptionalByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME)));
    }

    public static void authorize(String token) {
        try {
            var authorized = JWT.decode(token, SECRET);
            SecurityContextHolder
                .getContext()
                    .setAuthentication(authorized.getAuthentication());
        } catch (Exception exception) {
            throw new TokenExpiredOrInvalidException();
        }
    }

    public static Optional<Authorized> authorized() {
        try {
            var principal = getPrincipal();
            
            if (Objects.nonNull(principal) && principal instanceof Authorized authorized) {
                return Optional.of(authorized);
            }
            return Optional.empty();
        } catch (Exception exception) {
            return Optional.empty();
        }
        
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