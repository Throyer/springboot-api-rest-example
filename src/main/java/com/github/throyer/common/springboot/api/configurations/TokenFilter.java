package com.github.throyer.common.springboot.api.configurations;

import static com.github.throyer.common.springboot.api.utils.TokenUtils.recuperarTokenDaRequest;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.throyer.common.springboot.api.models.security.Authorized;
import com.github.throyer.common.springboot.api.services.security.TokenService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(TokenFilter.class);

    @Autowired
    private TokenService service;

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

                var session = service.toAuthorized(accessToken);
                fazerLogin(session);
                
            } catch (Exception exception) {                
                logger.error(exception.getMessage());
            }
        }

        filter.doFilter(req, res);
    }

    private void fazerLogin(Authorized session) {
        SecurityContextHolder
            .getContext()
                .setAuthentication(session.getAuthentication());
    }
}