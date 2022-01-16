package com.github.throyer.common.springboot.utils;

import static com.github.throyer.common.springboot.utils.Constants.SECURITY.JWT;

import java.time.LocalDateTime;
import java.util.List;

public class TokenUtils {

    public static String token(Integer expiration, String secret) {
        return token(expiration, secret, List.of("ADM"));
    }

    public static String token(Integer expiration, String secret, List<String> roles) {
        var token = JWT.encode(1L, roles, LocalDateTime.now().plusHours(expiration), secret);
        return String.format("Bearer %s", token);
    }
}