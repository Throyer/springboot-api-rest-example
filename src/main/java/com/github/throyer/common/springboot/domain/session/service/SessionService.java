package com.github.throyer.common.springboot.domain.session.service;

import com.github.throyer.common.springboot.domain.session.model.Authorized;
import com.github.throyer.common.springboot.domain.user.repository.UserRepository;
import com.github.throyer.common.springboot.utils.Authorization;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.github.throyer.common.springboot.constants.MESSAGES.INVALID_USERNAME;
import static com.github.throyer.common.springboot.constants.SECURITY.*;
import static com.github.throyer.common.springboot.utils.Messages.message;
import static com.github.throyer.common.springboot.utils.Responses.expired;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class SessionService implements UserDetailsService {

    private final UserRepository repository;

    public SessionService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(message(INVALID_USERNAME)));
        
        return new Authorized(user);
    }

    public static void authorize(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        if (PUBLIC_API_ROUTES.anyMatch(request)) {
            return;
        }

        var token = Authorization.extract(request);

        if (isNull(token)) {
            return;
        }

        try {
            var authorized = JWT.decode(token, TOKEN_SECRET);
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authorized.getAuthentication());
        } catch (Exception exception) {
            expired(response);
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
