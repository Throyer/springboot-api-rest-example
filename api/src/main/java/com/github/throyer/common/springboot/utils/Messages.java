package com.github.throyer.common.springboot.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class Messages {

    public static ResourceBundleMessageSource messageSource;

    @Autowired
    public Messages(ResourceBundleMessageSource resourceBundleMessageSource) {
        Messages.messageSource = resourceBundleMessageSource;
    }

    public static String message(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public static String message(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}