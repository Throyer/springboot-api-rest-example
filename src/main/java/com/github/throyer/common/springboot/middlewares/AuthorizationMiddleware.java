package com.github.throyer.common.springboot.middlewares;

import com.github.throyer.common.springboot.domain.session.service.SessionService;
import static java.util.Optional.ofNullable;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.util.Objects.isNull;
import org.springframework.core.annotation.Order;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
public class AuthorizationMiddleware extends OncePerRequestFilter {
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ACCEPTABLE_TOKEN_TYPE = "Bearer ";
    
    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filter
    )
        throws ServletException, IOException {

        ofNullable(authorization(request))
            .ifPresent(SessionService::authorize);

        filter.doFilter(request, response);
    }
    
    private static String authorization(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (tokenIsNull(token)) {
            return null;            
        }
        
        if (token.substring(7).equals("Bearer")) {
            return null;
        }

        return token.substring(7, token.length());
    }
    
    private static boolean tokenIsNull(String token) {
        return isNull(token) || token.isEmpty() || !token.startsWith(ACCEPTABLE_TOKEN_TYPE);
    }    
}