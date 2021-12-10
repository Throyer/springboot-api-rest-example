package com.github.throyer.common.springboot.domain.services.security;

import static com.github.throyer.common.springboot.utils.Constants.SECURITY.INVALID_USERNAME;
import static com.github.throyer.common.springboot.utils.Constants.SECURITY.JWT;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import com.github.throyer.common.springboot.domain.models.security.Authorized;
import com.github.throyer.common.springboot.domain.repositories.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityService.class);

    @Autowired
    UserRepository repository;

    private static String SECRET;

    public SecurityService(@Value("${token.secret}") String secret) {
        SecurityService.SECRET = secret;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new Authorized(repository.findOptionalByEmailFetchRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_USERNAME)));
    }

    public static void authorize(String token) {
        try {
            var authorized = JWT.decode(token, SECRET);
            SecurityContextHolder
                .getContext()
                    .setAuthentication(authorized.getAuthentication());
        } catch (Exception exception) {
            LOGGER.error("Token expired or invalid");
        }
    }

    public static Optional<Authorized> authorized() {
        try {
            var principal = getPrincipal();
            
            if (nonNull(principal) && principal instanceof Authorized authorized) {
                return of(authorized);
            }
            return empty();
        } catch (Exception exception) {
            return empty();
        }
        
    }

    private static Object getPrincipal() {
        var authentication = SecurityContextHolder.getContext()
            .getAuthentication();
        if (nonNull(authentication)) {
            return authentication.getPrincipal();
        }
        return null;
    } 
}