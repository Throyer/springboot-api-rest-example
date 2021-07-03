package com.github.throyer.common.springboot.api.middlewares;

import static com.github.throyer.common.springboot.api.domain.services.security.SecurityService.signIn;
import static com.github.throyer.common.springboot.api.utils.TokenUtils.recuperarTokenDaRequest;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthorizationMiddleware extends OncePerRequestFilter {
    
    private Logger logger = LoggerFactory.getLogger(AuthorizationMiddleware.class);

    @Override
    protected void doFilterInternal(
        HttpServletRequest req,
        HttpServletResponse res,
        FilterChain filter
    )
        throws ServletException, IOException {

        var accessToken = recuperarTokenDaRequest(req);

        if (Objects.nonNull(accessToken)) {
            
            try {
                signIn(accessToken);
            } catch (Exception exception) {                
                logger.error(exception.getMessage());
            }
        }

        filter.doFilter(req, res);
    }
}