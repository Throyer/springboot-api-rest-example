package com.github.throyer.common.springboot.utils;

import static com.github.throyer.common.springboot.utils.Constants.SECURITY.JWT;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import com.github.throyer.common.springboot.domain.models.entity.Role;

public class TokenUtils {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ACCEPTABLE_TOKEN_TYPE = "Bearer ";

    /**
     * Recupera o token de dentro do cabeçalho da request.
     * 
     * @param request {request que chegou na API}
     * @return null se o token estiver vazio ou for do tipo errado.
     */
    public static String authorization(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (tokenIsNull(token))
            return null;

        return token.substring(7, token.length());
    }

    /**
     * Verifica se o token esta vazio ou não é do tipo 'Bearer'.
     * 
     * @param token
     * @return true se o token for vazio ou não for bearer.
     */
    private static boolean tokenIsNull(String token) {
        return Objects.isNull(token) || token.isEmpty() || !token.startsWith(ACCEPTABLE_TOKEN_TYPE);
    }

    public static String token(Integer expiration, String secret) {
        return token(expiration, secret, List.of(new Role("ADM")));
    }

    public static String token(Integer expiration, String secret, List<Role> roles) {
        var token = JWT.encode(1L, roles, LocalDateTime.now().plusHours(expiration), secret);
        return String.format("Bearer %s", token);
    }
}