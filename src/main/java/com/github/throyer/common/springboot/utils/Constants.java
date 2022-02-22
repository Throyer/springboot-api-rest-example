package com.github.throyer.common.springboot.utils;

import com.github.throyer.common.springboot.domain.session.service.JsonWebToken;
import com.github.throyer.common.springboot.domain.shared.PublicRoutes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.github.throyer.common.springboot.domain.shared.PublicRoutes.create;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class Constants {

    public Constants(
        @Value("${token.secret}") String tokenSecret,
        @Value("${token.expiration-in-hours}") Integer tokenExpirationInHours,
        @Value("${token.refresh.expiration-in-days}") Integer refreshTokenExpirationInDays,
        @Value("${recovery.minutes-to-expire}") Integer recoveryMinutesToExpire,
        @Value("${bucket4j.filters[0].rate-limits[0].bandwidths[0].capacity}") Integer maxRequestsPerMinute
    ) {
        Constants.SECURITY.TOKEN_SECRET = tokenSecret;
        Constants.SECURITY.TOKEN_EXPIRATION_IN_HOURS = tokenExpirationInHours;
        Constants.SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS = refreshTokenExpirationInDays;

        Constants.MAIL.MINUTES_TO_EXPIRE_RECOVERY_CODE = recoveryMinutesToExpire;
        Constants.RATE_LIMIT.MAX_REQUESTS_PER_MINUTE = maxRequestsPerMinute;
    }

    public static class SECURITY {
        public static final PublicRoutes PUBLIC_API_ROUTES = create()
            .add(GET, "/api", "/api/documentation/**")
            .add(POST, "/api/users", "/api/sessions/**", "/api/recoveries/**", "/api/documentation/**");

        public static final Integer DAY_MILLISECONDS = 86400;
        public static final JsonWebToken JWT = new JsonWebToken();

        public static final Integer PASSWORD_STRENGTH = 10;
        public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(PASSWORD_STRENGTH);

        public static final String ROLES_KEY_ON_JWT = "roles";

        public static String TOKEN_SECRET;
        public static Integer TOKEN_EXPIRATION_IN_HOURS;
        public static Integer REFRESH_TOKEN_EXPIRATION_IN_DAYS;

        public static final String USERNAME_PARAMETER = "email";
        public static final String PASSWORD_PARAMETER = "password";

        public static final String HOME_URL = "/app";
        public static final String LOGIN_URL = "/app/login";
        public static final String LOGIN_ERROR_URL = LOGIN_URL + "?error=true";
        public static final String ACESSO_NEGADO_URL = LOGIN_URL + "?denied=true";
        public static final String LOGOUT_URL = "/app/logout";
        
        public static final String SESSION_COOKIE_NAME = "JSESSIONID";

        public static final String SECURITY_TYPE = "Bearer";
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String ACCEPTABLE_TOKEN_TYPE = SECURITY_TYPE + " ";
        public static final String CAN_T_WRITE_RESPONSE_ERROR = "can't write response error.";
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

        public static Integer MINUTES_TO_EXPIRE_RECOVERY_CODE = 20;
        public static final String SUBJECT_PASSWORD_RECOVERY_CODE = "Password recovery code";
        public static final String EMAIL_SENT_SUCCESSFULLY_MESSAGE_LOG_TEMPLATE = "email sent successfully to: %s";
        public static final String UNABLE_TO_SEND_EMAIL_MESSAGE_TEMPLATE = "Unable to send email to: %s";
    }

    public static class RATE_LIMIT {
        public static Integer MAX_REQUESTS_PER_MINUTE;
    }

    /**
     * Validation messages.
     * @see "resources/messages.properties"
     */
    public static class MESSAGES {
        public static final String NOT_AUTHORIZED_TO_LIST = "not.authorized.list";
        public static final String NOT_AUTHORIZED_TO_READ = "not.authorized.read";
        public static final String NOT_AUTHORIZED_TO_CREATE = "not.authorized.create";
        public static final String NOT_AUTHORIZED_TO_MODIFY = "not.authorized.modify";

        public static String EMAIL_ALREADY_USED_MESSAGE = "email.already-used.error.message";

        public static final String TYPE_MISMATCH_ERROR_MESSAGE = "type.mismatch.message";
        public static final String TOKEN_EXPIRED_OR_INVALID = "token.expired-or-invalid";
        public static final String TOKEN_HEADER_MISSING_MESSAGE = "token.header.missing";
        public static final String INVALID_USERNAME = "invalid.username.error.message";

        public static final String CREATE_SESSION_ERROR_MESSAGE = "create.session.error.message";
        public static final String REFRESH_SESSION_ERROR_MESSAGE = "refresh.session.error.message";
    }
}
