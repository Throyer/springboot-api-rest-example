package com.github.throyer.common.springboot.utils;

import com.github.throyer.common.springboot.domain.session.service.JsonWebToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Constants {
    public static class SECURITY {
        public static final JsonWebToken JWT = new JsonWebToken();
        public static final Long HOUR_IN_SECONDS = 3600L;
        public static final Integer DAY_MILLISECONDS = 86400;
        
        public static final Integer PASSWORD_STRENGTH = 10;
        public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(PASSWORD_STRENGTH);

        public static final String ROLES_KEY_ON_JWT = "roles";
        public static final String INVALID_USERNAME = "Invalid username.";
        public static final String CREATE_SESSION_ERROR_MESSAGE = "Invalid password or username.";
        public static final String REFRESH_SESSION_ERROR_MESSAGE = "Refresh token expired or invalid.";
    
        public static final String USERNAME_PARAMETER = "email";
        public static final String PASSWORD_PARAMETER = "password";

        public static final String HOME_URL = "/app";
        public static final String LOGIN_URL = "/app/login";
        public static final String LOGIN_ERROR_URL = LOGIN_URL + "?error=true";
        public static final String ACESSO_NEGADO_URL = LOGIN_URL + "?denied=true";
        public static final String LOGOUT_URL = "/app/logout";
        
        public static final String SESSION_COOKIE_NAME = "JSESSIONID";
        
        public static final String[] STATIC_FILES = {
            "/robots.txt",
            "/font/**",
            "/css/**",
            "/webjars/**",
            "/webjars/",
            "/js/**",
            "/favicon.ico",
            "/**.html",
            "/documentation/**"
        };    
    }
}
