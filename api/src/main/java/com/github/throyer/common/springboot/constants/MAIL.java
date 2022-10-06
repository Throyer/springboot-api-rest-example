package com.github.throyer.common.springboot.constants;

public class MAIL {
    private MAIL () {}
    
    public static final Boolean CONTENT_IS_HTML = true;
    public static final String ERROR_SMTP_AUTH = "Error on SMTP authentication.";
    public static final String ERROR_SENDING_EMAIL_MESSAGE = "Error sending email.";
    public static final String ERROR_SENDING_EMAIL_MESSAGE_TO = "Error sending email to: {}";
    public static final String EMAIL_SUCCESSFULLY_SENT_TO = "Email successfully sent to: {}";
    public static final String EMAIL_SENT_SUCCESSFULLY_MESSAGE_LOG_TEMPLATE = "email sent successfully to: %s";
    public static final String UNABLE_TO_SEND_EMAIL_MESSAGE_TEMPLATE = "Unable to send email to: %s";
}
