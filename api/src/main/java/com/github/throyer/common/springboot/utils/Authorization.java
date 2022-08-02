package com.github.throyer.common.springboot.utils;

import javax.servlet.http.HttpServletRequest;

import static com.github.throyer.common.springboot.constants.SECURITY.*;
import static java.util.Objects.isNull;

public class Authorization {
    private Authorization() { }

    public static String extract(HttpServletRequest request) {
        var token = request.getHeader(AUTHORIZATION_HEADER);

        if (tokenIsNull(token)) {
            return null;
        }

        if (token.substring(BEARER_WORD_LENGTH).equals(SECURITY_TYPE)) {
            return null;
        }

        return token.substring(BEARER_WORD_LENGTH);
    }

    public static boolean tokenIsNull(String token) {
        return isNull(token) || !token.startsWith(ACCEPTABLE_TOKEN_TYPE);
    }
}
