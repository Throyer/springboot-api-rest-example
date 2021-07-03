package com.github.throyer.common.springboot.api.utils;

import com.github.throyer.common.springboot.api.domain.services.security.JsonWebToken;

public class Constants {
    public class SECURITY {
        public static final JsonWebToken JWT = new JsonWebToken();
        public static final Long HOUR_IN_SECONDS = 3600L;
        public static final String ROLES_KEY_ON_JWT = "roles";
        public static final String INVALID_USERNAME = "Nome de usuário invalido.";
        public static final String CREATE_SESSION_ERROR_MESSAGE = "Senha ou Usuário inválidos.";
        public static final String REFRESH_SESSION_ERROR_MESSAGE = "Refresh token expirado ou inválido.";
    }
}
