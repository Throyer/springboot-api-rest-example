package com.github.throyer.example.modules.recoveries.models;

import org.thymeleaf.context.Context;

import com.github.throyer.example.modules.mail.models.Email;

public class RecoveryEmail implements Email {
    private final String destination;
    private final String subject;
    private final String name;
    private final String code;
    
    public RecoveryEmail(
        String destination,
        String subject,
        String name,
        String recoveryCode
    ) {
        this.destination = destination;
        this.subject = subject;
        this.name = name;
        this.code = recoveryCode;
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
        
        context.setVariable("name", this.name);        
        context.setVariable("code", this.code);
        
        return context;
    }
    
}
