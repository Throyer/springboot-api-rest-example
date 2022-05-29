package com.github.throyer.common.springboot.constants;

import com.github.throyer.common.springboot.domain.session.service.JsonWebToken;
import com.github.throyer.common.springboot.domain.shared.PublicRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import static com.github.throyer.common.springboot.domain.shared.PublicRoutes.create;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Component
public class SECURITY {

    @Autowired
    public SECURITY(
        @Value("${token.secret}") String tokenSecret,
        @Value("${token.expiration-in-hours}") Integer tokenExpirationInHours,
        @Value("${token.refresh.expiration-in-days}") Integer refreshTokenExpirationInDays
    ) {
        SECURITY.TOKEN_SECRET = tokenSecret;
        SECURITY.TOKEN_EXPIRATION_IN_HOURS = tokenExpirationInHours;
        SECURITY.REFRESH_TOKEN_EXPIRATION_IN_DAYS = refreshTokenExpirationInDays;
    }

    public static final String[] STATIC_FILES = {
        "/robots.txt",
        "/font/**",
        "/css/**",
        "/webjars/**",
        "/js/**",
        "/favicon.ico",
        "/**.html",
        "/documentation/**"
    };

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
    public static final Integer BEARER_WORD_LENGTH = SECURITY_TYPE.length();
}
