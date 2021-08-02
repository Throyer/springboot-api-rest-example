package com.github.throyer.common.springboot.api.domain.validation;

import static com.github.throyer.common.springboot.api.utils.JsonUtils.toJson;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class TokenExpiredOrInvalidException extends RuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenExpiredOrInvalidException.class);

    public void unauthorized(HttpServletResponse response) {
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(toJson(new SimpleError("token expired or invalid", HttpStatus.UNAUTHORIZED)));
            response.getWriter().flush();
        } catch (Exception exception) {
            LOGGER.error("can't write response error on token expired or invalid exception", exception);
        }
    }
}



    