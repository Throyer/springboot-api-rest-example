package com.github.throyer.common.springboot.api.models.validation.constraints;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailExists {

    String message() default "Este e-mail já utilizado por outro usuario. Por favor, utilize um e-mail diferente.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
