package com.github.throyer.common.springboot.domain.models.emails;

import com.github.throyer.common.springboot.domain.services.email.Email;

import org.thymeleaf.context.Context;

public class RecoveryEmail implements Email {

    private String destination;
    private String subject;
    private String recoveryCode;

    public RecoveryEmail(String destination, String subject, String recoveryCode) {
        this.destination = destination;
        this.subject = subject;
        this.recoveryCode = recoveryCode;
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    @Override
    public String getTemplate() {
        return "recovery-password";
    }

    @Override
    public Context getContext() {
        var context = new Context();
        context.setVariable("code", recoveryCode);
        return context;
    }
    
}
