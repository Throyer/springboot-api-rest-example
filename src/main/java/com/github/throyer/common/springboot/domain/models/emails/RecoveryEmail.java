package com.github.throyer.common.springboot.domain.models.emails;

import com.github.throyer.common.springboot.domain.services.email.Email;

import org.thymeleaf.context.Context;

public class RecoveryEmail implements Email {
    
    private static final Integer FIRST = 0;
    private static final Integer SECOND = 1;
    private static final Integer THIRD = 2;
    private static final Integer FOURTH = 3;

    private final String destination;
    private final String subject;
    private final String name;
    
    private final String first;
    private final String second;
    private final String third;
    private final String fourth;

    public RecoveryEmail(
        String destination,
        String subject,
        String name,
        String recoveryCode
    ) {
        this.destination = destination;
        this.subject = subject;
        this.name = name;
        
        var code = recoveryCode.split("");
        
        this.first = code[FIRST];
        this.second = code[SECOND];
        this.third = code[THIRD];
        this.fourth = code[FOURTH];
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
        
        context.setVariable("first", this.first);
        context.setVariable("second", this.second);
        context.setVariable("third", this.third);
        context.setVariable("fourth", this.fourth);
        
        return context;
    }
    
}
