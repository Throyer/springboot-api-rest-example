package com.github.throyer.example.modules.shared.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class InternationalizationUtils {

    public static ResourceBundleMessageSource messageSource;

    @Autowired
    public InternationalizationUtils(ResourceBundleMessageSource resourceBundleMessageSource) {
        InternationalizationUtils.messageSource = resourceBundleMessageSource;
    }

    public static String message(String key) {
        return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
    }

    public static String message(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }
}