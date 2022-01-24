package com.github.throyer.common.springboot.utils;

import com.github.throyer.common.springboot.domain.session.service.JsonWebToken;
import com.github.throyer.common.springboot.errors.Error;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Constants {

    public Constants(
        @Value("${token.secret}") String tokenSecret,
        @Value("${token.expiration-in-hours}") Integer tokenExpirationInHours,
        @Value("${token.refresh.expiration-in-days}") Integer refreshTokenExpirationInDays,
        @Value("${recovery.minutes-to-expire}") Integer recoveryMinutesToExpire
    ) {
        Constants.SECURITY.TOKEN_SECRET = tokenSecret;
        Constants.SECURITY.TOKEN_EXPIRATION_IN_HOURS = tokenExpirationInHours;
        Constants.SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS = refreshTokenExpirationInDays;

        Constants.MAIL.MINUTES_TO_EXPIRE_RECOVERY_CODE = recoveryMinutesToExpire;
    }

    public static class SECURITY {
        public static final Integer DAY_MILLISECONDS = 86400;
        public static final JsonWebToken JWT = new JsonWebToken();
        public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10);

        public static final String ROLES_KEY_ON_JWT = "roles";
        public static final String INVALID_USERNAME = "Invalid username.";
        public static final String CREATE_SESSION_ERROR_MESSAGE = "Invalid password or username.";
        public static final String REFRESH_SESSION_ERROR_MESSAGE = "Refresh token expired or invalid.";

        public static String TOKEN_SECRET;
        public static Integer TOKEN_EXPIRATION_IN_HOURS;
        public static Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;

        public static final String TOKEN_EXPIRED_MESSAGE = "Token expired or invalid";

        public static final String USERNAME_PARAMETER = "email";
        public static final String PASSWORD_PARAMETER = "password";

        public static final String HOME_URL = "/app";
        public static final String LOGIN_URL = "/app/login";
        public static final String LOGIN_ERROR_URL = LOGIN_URL + "?error=true";
        public static final String ACESSO_NEGADO_URL = LOGIN_URL + "?denied=true";
        public static final String LOGOUT_URL = "/app/logout";
        
        public static final String SESSION_COOKIE_NAME = "JSESSIONID";

        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String BEARER_TOKEN = "Bearer";
        public static final String ACCEPTABLE_TOKEN_TYPE = BEARER_TOKEN + " ";
        public static final Integer BEARER_WORD_LENGTH = 7;
        
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

    public static class MAIL {
        public static final Boolean CONTENT_IS_HTML = true;
        public static final String ERROR_SENDING_EMAIL_MESSAGE = "Error sending email.";
        public static final String EMAIL_SUCCESSFULLY_SENT_TO = "Email successfully sent to: {}";

        public static String EMAIL_FIELD = "email";
        public static String EMAIL_ALREADY_USED_MESSAGE = "This email has already been used by another user. Please use a different email.";
        public static final List<Error> EMAIL_ERROR = List.of(new Error(EMAIL_FIELD, EMAIL_ALREADY_USED_MESSAGE));

        public static Integer MINUTES_TO_EXPIRE_RECOVERY_CODE = 20;
        public static final String SUBJECT_PASSWORD_RECOVERY_CODE = "password recovery code";
        public static final String EMAIL_SENT_SUCCESSFULLY_MESSAGE_LOG_TEMPLATE = "email sent successfully to: %s";
        public static final String UNABLE_TO_SEND_EMAIL_MESSAGE_TEMPLATE = "Unable to send email to: %s";

    }
}
