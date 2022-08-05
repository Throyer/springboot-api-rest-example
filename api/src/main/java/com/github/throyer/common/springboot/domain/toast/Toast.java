package com.github.throyer.common.springboot.domain.toast;

import lombok.Data;
import org.springframework.validation.ObjectError;

@Data
public class Toast {

    private String message;
    private String type;

    private Toast(String message, String type) {
        this.message = message;
        this.type = type;
    }

    public static Toast of(String message, Type type) {
        return new Toast(message, type.name);
    }
    
    public static Toast of(ObjectError error) {
        return of(error.getDefaultMessage(), Type.DANGER);
    }
}
