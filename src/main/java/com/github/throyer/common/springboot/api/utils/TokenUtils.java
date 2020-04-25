package com.github.throyer.common.springboot.api.utils;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ACEPTED_TOKEN_TYPE = "Bearer ";

    /**
     * Recupera o token de dentro do cabeçalho da request.
     * @param request {request que chegou na API}
     * @return null se o token estiver vazio ou for do tipo errado.
     */
    public static String recuperarTokenDaRequest(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (tokenIsNull(token)) return null;
        
        return token.substring(7, token.length());
    }

    /**
     * Verifiva se o token esta vazio ou não é
     * do tipo 'Bearer'.
     * @param token
     * @return true se o token for fazio ou não for bearer.
     */
    private static boolean tokenIsNull(String token) {
        return Objects.isNull(token) || token.isEmpty() || !token.startsWith(ACEPTED_TOKEN_TYPE);
    }
}