package com.github.throyer.example.api.shared.i18n;

import org.springframework.context.support.ResourceBundleMessageSource;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

public class InternationalizationImpl implements Internationalization {
  private final ResourceBundleMessageSource messageSource;

  public InternationalizationImpl(ResourceBundleMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public String message(String key) {
    return messageSource.getMessage(key, null, getLocale());
  }

  @Override
  public String message(String key, Object... args) {
    return messageSource.getMessage(key, args, getLocale());
  }
}
