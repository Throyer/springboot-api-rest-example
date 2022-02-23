package com.github.throyer.common.springboot.constants;

/**
 * Validation messages.
 * @see "resources/messages.properties"
 */
public class MESSAGES {
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
